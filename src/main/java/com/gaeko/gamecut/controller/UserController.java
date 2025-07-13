package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.jwt.JwtUtil;
import com.gaeko.gamecut.repository.UserRepository;
import com.gaeko.gamecut.service.GuessTheRankService;
import com.gaeko.gamecut.service.PointService;
import com.gaeko.gamecut.service.SmsService;
import com.gaeko.gamecut.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest; // 2025년 7월 7일 수정됨 - HttpServletRequest import 추가
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final SmsService smsService;
    private final PointService pointService;
    @GetMapping("/user/listUser")
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @PostMapping("/user/join")
    public Map<String, Object> join(@RequestBody UserDTO dto) {
        System.out.println("user/join으로 넘어옴.");
        boolean result = userService.register(dto);
        return Map.of("success", result);
    }

//    @PostMapping("/user/login")
//    public Map<String, Object> login(@RequestBody Map<String, String> body) {
//        return userService.loginWithToken(body.get("userId"), body.get("pwd"));
//    }

    @PostMapping("/user/findPassword")
    public Map<String, Object> findPassword(@RequestBody Map<String, String> body) {
        boolean result = userService.findPassword(body.get("userId"), body.get("email"));
        return Map.of("success", result);
    }


    // 아이디 중복확인
    @GetMapping("/user/checkUserId")
    public Map<String, Boolean> checkUserId(@RequestParam String userId) {
        boolean exists = userService.isUserIdExists(userId);
        return Map.of("exists", exists);
    }

    // 닉네임 중복확인
    @GetMapping("/user/checkUserNickname")
    public Map<String, Boolean> checkUserNickname(@RequestParam String userNickname) {
        boolean exists = userService.isUserNicknameExists(userNickname);
        return Map.of("exists", exists);
    }


    //유저 정보가저오기

    @GetMapping("/user/myinfo")
    public UserDTO getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();  // 현재 로그인된 유저의 아이디
        
        return userService.findUserByUserId(userId);
    }
    
    
    

    @PostMapping("/user/oauth/google")
    public Map<String, Object> googleLogin(@RequestBody Map<String, String> body) {
        String token = body.get("accessToken");
        return userService.googleLogin(token);
    }

    @PostMapping("/user/oauth/naver")
    public Map<String, Object> naverLogin(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String state = body.get("state");
        return userService.naverLogin(code, state);
    }

    
    @PostMapping("/user/email/send")
    public Map<String, Object> sendEmailCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return userService.sendEmailCode(email);
    }

    @PostMapping("/user/email/verify")
    public Map<String, Object> verifyEmailCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        return userService.verifyEmailCode(email, code);
    }


    @GetMapping("/user/{userNo}")
    public UserDTO findUserByUserNo(@PathVariable Integer userNo) {
        System.out.println(userService.findUserByUserNo(userNo));
        return userService.findUserByUserNo(userNo);
    }

    @PutMapping("/user/delete/{userid}")
    public void deleteUser(@PathVariable String userid) {
        userService.userDelete(userid);
    }
    
    
    @PostMapping("/user/refresh")
    public ResponseEntity<Map<String, Object>> refreshAccessToken(@CookieValue("refreshToken") String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.ok(Map.of("success", false, "message", "쿠키가 없음"));
        }

        try {
            String userId = jwtUtil.getUserId(refreshToken);
            String stored = userService.getRefreshTokenForUser(userId);

            if (!refreshToken.equals(stored)) {
                return ResponseEntity.ok(Map.of("success", false, "message", "유효하지 않은 토큰"));
            }
            
            String newAccessToken = jwtUtil.createToken(userId, "USER");

            ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(false)  // JavaScript에서 JWT 디코딩을 위해 false
                .path("/")
                .maxAge(60 * 15)  // 15분
                .secure(false)    // 프로덕션에서는 true
                .sameSite("Lax")
                .build();
            return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .body(Map.of("success", true, "token", newAccessToken));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("success", false, "message", "토큰 만료 또는 위조됨"));
        }
    }

    
    @PostMapping("/user/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        Map<String, Object> result = userService.loginWithToken(body.get("userId"), body.get("pwd"));

        if (!(Boolean) result.get("success")) {
            return ResponseEntity.ok(result);
        }

        String accessToken = (String) result.get("accessToken");
        String refreshToken = (String) result.get("refreshToken");

        // refreshToken 쿠키
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .path("/")
            .maxAge(60 * 60 * 24 * 7) // 7일
            .secure(false)
            .sameSite("Lax")
            .build();

        // accessToken 쿠키
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
            .httpOnly(false)  // JavaScript에서 접근 가능하도록
            .path("/")
            .maxAge(60 * 15)  // 15분
            .secure(false)
            .sameSite("Lax")
            .build();

        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .header(HttpHeaders.SET_COOKIE, accessCookie.toString()) // 두 개의 쿠키 모두 설정
            .body(result);
    }
    
    @PostMapping("/user/findPasswordByPhone")
    public Map<String, Object> findPasswordByPhone(@RequestBody Map<String, String> body) {
        boolean result = smsService.findPasswordByPhone(body.get("userId"), body.get("phone"));
        return Map.of("success", result);
    }
    
    
    @PutMapping("/user/change-password")
    public Map<String, Object> changePassword(@RequestBody Map<String, String> body) {
        // 기존: String userId = body.get("userId");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // ✅ JWT에서 가져온 userId

        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        boolean result = userService.changePassword(userId, currentPassword, newPassword);
        return result
            ? Map.of("success", true)
            : Map.of("success", false, "message", "현재 비밀번호가 일치하지 않습니다.");
    }

    
    
    
    

    @PostMapping("/user/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. SecurityContextHolder로부터 유저 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // 로그인된 userId

        // 2. refreshToken 제거 (서버 쪽 메모리 or Redis 등에서)
        userService.removeRefreshToken(userId);

        // 3. 쿠키 삭제 (maxAge = 0)
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)  // 쿠키 제거
                .sameSite("Lax")
                .secure(false) // 배포시 true로
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("success", true, "message", "로그아웃 되었습니다."));
    }


    @PutMapping(
        value = "/user",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE // 2025년 7월 7일 수정됨 - multipart 지원 추가
    )
    public ResponseEntity<Map<String, Object>> updateUser(
            @RequestParam(value = "userName", required = false) String newUserName, // 2025-07-10 추가됨 - userName 파라미터 추가
            @RequestParam(value = "userId", required = false) String newUserId, // 2025년 7월 7일 수정됨 - RequestParam으로 변경 (문자열 매개변수)
            @RequestParam(value = "userNickname", required = false) String newNickname,
            @RequestParam(value = "phone", required = false) String newPhone, // 2025-07-10 추가됨 - phone 파라미터 추가
            @RequestParam(value = "email", required = false) String newEmail, // 2025-07-10 추가됨 - email 파라미터 추가
            @RequestParam(value = "deletePhoto", required = false) Boolean deletePhoto, // 2025년 7월 7일 수정됨 - RequestParam으로 변경
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage // 2025년 7월 7일 수정됨 - 파일만 RequestPart 유지
    ) throws IOException { // 2025년 7월 7일 수정됨 - IOException 추가
        
        // 2025년 7월 7일 수정됨 - SecurityContextHolder에서 Authentication 가져오기 (getUserInfo()와 동일 방식)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String oldUserId = auth.getName();
        
        // 2025년 7월 7일 수정됨 - 프로필 이미지 업데이트 (변경사항이 있는 경우만)
        if (profileImage != null || Boolean.TRUE.equals(deletePhoto)) {
            userService.updateProfilePhoto(
                oldUserId,
                Boolean.TRUE.equals(deletePhoto),
                profileImage
            );
        }
        
        // 2025-07-10 수정됨 - 사용자 기본 정보 업데이트 (변경사항이 있는 경우만)
        if (newUserName != null || newUserId != null || newNickname != null || newPhone != null || newEmail != null) {
            // 기존 사용자 정보 가져오기
            UserDTO currentUser = userService.findUserByUserId(oldUserId);
            
            // 기본값 설정 (변경하지 않으면 기존 값 유지)
            if (newUserName == null) newUserName = currentUser.getUserName();
            if (newUserId == null) newUserId = oldUserId;
            if (newNickname == null) newNickname = currentUser.getUserNickname();
            if (newPhone == null) newPhone = currentUser.getPhone();
            if (newEmail == null) newEmail = currentUser.getEmail();
            
            // 사용자 정보 업데이트
            userService.updateUserInfo(oldUserId, newUserName, newUserId, newNickname, newPhone, newEmail);

            // 새 토큰 생성 (ID가 변경된 경우만)
            if (!oldUserId.equals(newUserId)) {
                String newAccessToken = jwtUtil.createToken(newUserId, "USER");
                String newRefreshToken = jwtUtil.createRefreshToken(newUserId);

                // 서비스 메서드로 refreshTokenStore 갱신
                userService.replaceRefreshToken(oldUserId, newUserId, newRefreshToken);

                // 쿠키에 새 토큰 세팅
                ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                    .httpOnly(true).path("/").maxAge(7 * 24 * 3600).sameSite("Lax").build();
                ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                    .httpOnly(false).path("/").maxAge(15 * 60).sameSite("Lax").build();

                return ResponseEntity
                    .ok()
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                    .body(Map.of("success", true, "accessToken", newAccessToken));
            }
        }
        
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/user/updatePoint")
    public ResponseEntity<?> updatePoint(
            @AuthenticationPrincipal UserDetails loginUser,
            @RequestParam Integer point,  // 직접 음수/양수로 받음
            @RequestParam String reason,
            @RequestParam Integer recievedUserNo
    ) {
        UserDTO user;
        String action;
        if(recievedUserNo != null && recievedUserNo != 0) {
            user = userService.findUserByUserNo(recievedUserNo);
            // 포인트 변동 (직접 더하기)
            user.setUserPoint(user.getUserPoint() + point);
            userService.updateUser(user);
            
            // 이력 저장 (그대로 저장)
            pointService.insertHistory(user, point, reason);
            
            action = point > 0 ? "획득" : "사용";
        } else {
            Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());
            user = userService.findUserByUserNo(userNo);
            // 포인트 변동 (직접 더하기)
            user.setUserPoint(user.getUserPoint() + point);
            userService.updateUser(user);
            
            // 이력 저장 (그대로 저장)
            pointService.insertHistory(user, point, reason);
            
            action = point > 0 ? "획득" : "사용";
        }
        return ResponseEntity.ok(Map.of(
            "success", true, 
            "message", "포인트 " + action + " 완료",
            "changedPoint", Math.abs(point),
            "currentPoint", user.getUserPoint()
        ));
    }
}
