package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.entity.Photo;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.jwt.JwtUtil;
import com.gaeko.gamecut.mapper.UserMapper;
import com.gaeko.gamecut.repository.FileRepository;
import com.gaeko.gamecut.repository.PhotoRepository;
import com.gaeko.gamecut.repository.UserRepository;
import com.gaeko.gamecut.util.EmailUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;
    private final JwtUtil jwtUtil;
    private final Map<String, String> refreshTokenStore = new HashMap<>();
    private final Map<String, String> emailVerificationMap = new HashMap<>();
    private FileUploadService fileUploadService;
    private final FileRepository fileRepository;

    // 이 세터 주입으로 순환 참조 깨기
    @Autowired
    public void setFileUploadService(@Lazy FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    public String getRefreshTokenForUser(String userId) {
        return refreshTokenStore.get(userId);
    }

    public UserDTO findUserByUserNo(Integer userNo) {
        User user = userRepository.findUserByUserNo(userNo);
        return userMapper.toDTO(user);
    }

    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toDTOs(users);
    }

    public void saveUser(UserDTO userDTO, Integer photoNo) {
        User user = userMapper.toEntity(userDTO);
        if (photoNo != null) {
            Photo photo = photoRepository.findById(photoNo).orElse(null);
            user.setPhoto(photo);
        }
        userRepository.save(user);
    }

    public boolean register(UserDTO dto) {
        if (userRepository.findByUserId(dto.getUserId()).isPresent()) {
            return false;
        }
        User user = User.builder()
                .userId(dto.getUserId())
                .userPwd(passwordEncoder.encode(dto.getUserPwd()))
                .userName(dto.getUserName())
                .userNickname(dto.getUserNickname())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .isSocial("basic")
                .role("USER")
                .userPoint(1000)
                .build();
        userRepository.save(user);
        return true;
    }

    public boolean login(String userId, String password) {
        Optional<User> userOpt = userRepository.findByUserId(userId);
        return userOpt.filter(user -> passwordEncoder.matches(password, user.getUserPwd())).isPresent();
    }

    public Map<String, Object> loginWithToken(String userId, String password) {
        Optional<User> userOpt = userRepository.findByUserId(userId);
        if (userOpt.isEmpty()) return Map.of("success", false);
        User user = userOpt.get();
        
        // 🔒 탈퇴한 유저인지 확인
        if (user.getUserDeleteDate() != null) {
            return Map.of("success", false, "message", "탈퇴한 사용자입니다.");
        }

        if (!passwordEncoder.matches(password, user.getUserPwd())) {
            return Map.of("success", false, "message", "비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtUtil.createToken(user.getUserId(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

        refreshTokenStore.put(user.getUserId(), refreshToken);

        return Map.of(
                "success", true,
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "userId", user.getUserId(),
                "userNickname", user.getUserNickname(),
                "userNo", user.getUserNo()
        );
    }

    public UserDTO findUserByUserId(String userId) {
        Optional<User> userOpt = userRepository.findByUserId(userId);
        return userOpt.map(userMapper::toDTO).orElse(null);
    }

    public boolean isUserIdExists(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    public boolean isUserNicknameExists(String userNickname) {
        return userRepository.findByUserNickname(userNickname).isPresent();
    }

    public boolean findPassword(String userId, String email) {
        Optional<User> userOpt = userRepository.findByUserIdAndEmail(userId, email);
        if (userOpt.isEmpty()) return false;

        String tempPwd = generateTempPassword();
        userOpt.get().setUserPwd(passwordEncoder.encode(tempPwd));
        userRepository.save(userOpt.get());

        emailUtil.sendEmail(email, "임시 비밀번호 발급", "임시 비밀번호: " + tempPwd);
        return true;
    }

    private String generateTempPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // Google Login (access_token 기반)
    public Map<String, Object> googleLogin(String accessToken) {
        String phone = "000-0000-0000";
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map<String, Object> userInfo = response.getBody();
            String googleId = (String) userInfo.get("sub");
            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");

            if (email == null) email = googleId + "@googleuser.com";
            if (name == null || name.isEmpty()) name = email;

            Optional<User> userOpt = userRepository.findByUserId(googleId);
            User user;
            if (userOpt.isEmpty()) {
                user = User.builder()
                        .userId(googleId)
                        .userPwd("SOCIAL_LOGIN")
                        .userName(name)
                        .userNickname(name)
                        .email(email)
                        .phone(phone)
                        .isSocial("google")
                        .role("USER")
                        .userPoint(1000)
                        .build();
                userRepository.save(user);
            } else {
                user = userOpt.get();
            }

            String jwt = jwtUtil.createToken(user.getUserId(), user.getRole());

            return Map.of("success", true, "token", jwt, "userId", user.getUserId(), "userNickname", user.getUserNickname());
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "message", "구글 로그인 실패");
        }
    }

    public Map<String, Object> naverLogin(String code, String state) {
        String phone = "000-0000-0000";
        try {
            String clientId = "CQbPXwMaS8p6gHpnTpsS";
            String clientSecret = "vGaGmanh_8";
            String redirectUri = "http://localhost:5173/naver/callback";

            String tokenUrl = "https://nid.naver.com/oauth2.0/token" +
                    "?grant_type=authorization_code" +
                    "&client_id=" + clientId +
                    "&client_secret=" + clientSecret +
                    "&code=" + code +
                    "&state=" + state;

            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> tokenResponse = restTemplate.getForObject(tokenUrl, Map.class);
            String accessToken = (String) tokenResponse.get("access_token");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> profileResponse = restTemplate.exchange(
                    "https://openapi.naver.com/v1/nid/me", HttpMethod.GET, entity, Map.class);

            Map<String, Object> responseMap = (Map<String, Object>) profileResponse.getBody().get("response");
            String naverId = (String) responseMap.get("id");
            String nickname = (String) responseMap.get("nickname");
            String email = (String) responseMap.get("email");
            String name = (String) responseMap.get("name");

            if (email == null) email = naverId + "@naver.com";
            if (nickname == null || nickname.isEmpty()) nickname = email;
            if (name == null || name.isEmpty()) name = nickname;

            Optional<User> userOpt = userRepository.findByUserId(naverId);
            User user;
            if (userOpt.isEmpty()) {
                user = User.builder()
                        .userId(naverId)
                        .userPwd("SOCIAL_LOGIN")
                        .userName(name)
                        .userNickname(nickname)
                        .email(email)
                        .phone(phone)
                        .isSocial("naver")
                        .role("USER")
                        .userPoint(1000)
                        .build();
                userRepository.save(user);
            } else {
                user = userOpt.get();
            }

            String jwt = jwtUtil.createToken(user.getUserId(), user.getRole());

            return Map.of("success", true, "token", jwt, "userId", user.getUserId(), "userNickname", user.getUserNickname());
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "message", "네이버 로그인 실패");
        }
    }

    public Map<String, Object> sendEmailCode(String email) {
        String code = generateCode();
        emailVerificationMap.put(email, code);
        emailUtil.sendEmail(email, "인증코드 발송", "인증코드는: " + code);
        return Map.of("success", true, "code", code);
    }

    public Map<String, Object> verifyEmailCode(String email, String inputCode) {
        String savedCode = emailVerificationMap.get(email);
        if (savedCode != null && savedCode.equals(inputCode)) {
            emailVerificationMap.remove(email);
            return Map.of("success", true);
        }
        return Map.of("success", false);
    }

    private String generateCode() {
        Random rnd = new Random();
        int code = 100000 + rnd.nextInt(900000);
        return String.valueOf(code);
    }

    public Integer userNoFindByUserName(String username) {
        return userRepository.findUserNoByUserId(username);
    }
    public void userDelete(String userid) {
        userRepository.userDelete(userid);
    }
    
    
    
    public boolean changePassword(String userId, String currentPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByUserId(userId);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        if (!passwordEncoder.matches(currentPassword, user.getUserPwd())) {
            return false;
        }

        user.setUserPwd(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
    
    
    public void removeRefreshToken(String userId) {
        refreshTokenStore.remove(userId);
    }



    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder
            .getContext()
            .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UsernameNotFoundException("인증된 사용자가 없습니다.");
        }

        // Principal에서 아이디(userId)를 추출
        Object principal = auth.getPrincipal();
        String userId;
        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }

        // Repository의 findByUserId를 호출
        return userRepository.findByUserId(userId)
            .orElseThrow(() ->
                new UsernameNotFoundException("해당 사용자 없음: " + userId)
            );
    }

    // [내 정보 수정] 닉네임, 이름 바꾸기 
    public boolean updateUserIdNickname(
        String currentUserId,
        String newUserId,
        String newNickname
    ) {
        User user = userRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + currentUserId));

        // userId 중복 검사
        if (!currentUserId.equals(newUserId) && isUserIdExists(newUserId)) {
            throw new IllegalArgumentException("User ID already in use: " + newUserId);
        }
        // 닉네임 중복 검사
        if (!user.getUserNickname().equals(newNickname)
                && isUserNicknameExists(newNickname)) {
            throw new IllegalArgumentException("Nickname already in use: " + newNickname);
        }

        user.setUserId(newUserId);
        user.setUserNickname(newNickname);
        userRepository.save(user);
        return true;
    }

    /**
     * 기존 userId 키로 저장된 refreshToken을 삭제하고,
     * newUserId 키로 새 refreshToken을 저장합니다.
     */
    public void replaceRefreshToken(String oldUserId, String newUserId, String newRefreshToken) {
        // 기존 토큰 제거
        refreshTokenStore.remove(oldUserId);
        // 새 토큰 저장
        refreshTokenStore.put(newUserId, newRefreshToken);
    }


    /**
     * 프로필 사진만 업데이트하거나 삭제
     *
     * @param userId        JWT에서 추출된 로그인된 사용자 ID
     * @param deletePhoto   true면 사진 삭제, false면 업로드 처리
     * @param profileImage  새로 업로드된 파일 (없으면 null)
     * @return              성공 여부
     * @throws IOException  파일 저장 중 I/O 오류 발생 시
     */
    public boolean updateProfilePhoto(
        String userId,
        boolean deletePhoto,
        MultipartFile profileImage
    ) throws IOException {
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다: " + userId));

        if (profileImage != null && !profileImage.isEmpty()) {
            FileDTO dto = fileUploadService.store(profileImage);
            File   savedFile = fileRepository.findById(dto.getAttachNo())
                .orElseThrow(() ->
                    new NoSuchElementException("저장된 파일을 찾을 수 없습니다: " + dto.getAttachNo())
                );

            Photo photo = Photo.builder()
                               .attachFile(savedFile)
                               .build();
            photoRepository.save(photo);
            user.setPhoto(photo);

        } else if (deletePhoto) {
            user.setPhoto(null);
        }

        userRepository.save(user);
        return true;
    }
    

}
