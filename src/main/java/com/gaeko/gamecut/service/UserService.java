package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.UserDTO;

import com.gaeko.gamecut.entity.Photo;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.jwt.JwtUtil;
import com.gaeko.gamecut.mapper.UserMapper;
import com.gaeko.gamecut.repository.PhotoRepository;
import com.gaeko.gamecut.repository.UserRepository;
import com.gaeko.gamecut.util.EmailUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;
    private final JwtUtil jwtUtil;  // 🔥 추가
    
    


    public UserDTO findUserByUserNo(Integer userNo) {
        User user = userRepository.findUserByUserNo(userNo);
        return userMapper.toDTO(user);
    }

    public List<UserDTO> findAll() {
        // DB에서 모든 User 엔티티를 가져온 뒤,
        // UserMapper의 toDTOs(List<User>)를 호출해야 합니다.
        List<User> users = userRepository.findAll();
        return userMapper.toDTOs(users);
    }

    //DB에 유저정보 저장
    public void saveUser(UserDTO userDTO, Integer photoNo) {
        User user = userMapper.toEntity(userDTO);

        if (photoNo != null) {
            Photo photo = photoRepository.findById(photoNo).orElse(null);
            user.setPhoto(photo);
        }

        userRepository.save(user);
    }
    
    
 // 회원가입
    public boolean register(UserDTO dto) {
    	System.out.println("UserService의 register로 넘어옴.");
       if (userRepository.findByUserId(dto.getUserId()).isPresent()) {
            return false;  // 중복 아이디 검사
        }
        System.out.println("id중복검사 완료.");
        

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

    // 로그인
    public boolean login(String userId, String password) {
        Optional<User> userOpt = userRepository.findByUserId(userId);
        if (userOpt.isEmpty()) return false;
        return passwordEncoder.matches(password, userOpt.get().getUserPwd());
    }

    public Map<String, Object> loginWithToken(String userId, String password) {
        Optional<User> userOpt = userRepository.findByUserId(userId);
        
        if (userOpt.isEmpty()) {
            return Map.of("success", false);
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getUserPwd())) {
            return Map.of("success", false);
        }

        String token = jwtUtil.createToken(user.getUserId(), user.getRole());
        
        // ✅ 콘솔 로그 추가
        System.out.println(user.getUserNickname() + "님 로그인 성공!");
        
        return Map.of("success", true, "token", token , "userId", user.getUserId(), "userNinkname", user.getUserNickname());
    }
    
    public UserDTO findUserByUserId(String userId) {
        Optional<User> userOpt = userRepository.findByUserId(userId);
        return userOpt.map(userMapper::toDTO).orElse(null);
    }
    



    
    
    // 비밀번호 찾기
    public boolean findPassword(String userId, String email) {
        Optional<User> userOpt = userRepository.findByUserIdAndEmail(userId, email);
        if (userOpt.isEmpty()) return false;

        String tempPwd = generateTempPassword();
        userOpt.get().setUserPwd(passwordEncoder.encode(tempPwd));
        userRepository.save(userOpt.get());

        emailUtil.sendEmail(email, "임시 비밀번호 발급", "임시 비밀번호: " + tempPwd);
        return true;
    }

    // 임시 비밀번호 생성
    private String generateTempPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
        
    }

}
