package com.gaeko.gamecut.dto;

import lombok.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {  // ← 클래스명 그대로 유지
    private Integer userNo;
    private String userId;
    private String userPwd;     // ← 이 줄만 주석처리!
    private String userName;
    private String userNickname;
    private String phone;
    private String email;
    private Date userCreateDate;
    private Date userDeleteDate;
    private String isSocial;
    private String role;
    private Integer userPoint;
    private Integer itemNo;
    private Integer photoNo;
}

// // 회원가입/로그인용 별도 DTO
// @Getter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class UserRegistrationDTO {
//     private String userId;
//     private String userPwd;      // 입력용에서만 사용
//     private String userName;
//     private String userNickname;
//     private String phone;
//     private String email;
//     private String isSocial;
// }

// // 목록 조회용 간단 DTO  
// @Getter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class UserListDTO {
//     private Integer userNo;
//     private String userNickname;
//     private String profileImageUrl;
//     private Integer userPoint;
//     private Date userCreateDate;
// }