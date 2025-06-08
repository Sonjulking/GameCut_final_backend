package com.gaeko.gamecut.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer userNo;

    private String userId;

    // 보안상 직접 노출을 원치 않는다면 제외해도 됩니다.
    private String userPwd;

    private String userName;
    private String userNickname;

    private String phone;
    private String email;

    private LocalDateTime userCreateDate;
    private LocalDateTime userDeleteDate;

    private String isSocial;
    private String role;

    private Integer userPoint;

    // 연관된 Item 엔티티를 ItemDTO로 매핑
    private ItemDTO item;

    // 연관된 Photo 엔티티(프로필 사진)를 PhotoDTO로 매핑
    private PhotoDTO photo;
}
