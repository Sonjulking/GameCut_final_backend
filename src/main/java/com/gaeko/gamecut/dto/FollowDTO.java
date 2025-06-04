package com.gaeko.gamecut.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowDTO {
    private Integer followeeNo;
    private Integer followerNo;

    private String followeeNickname; // 팔로잉 대상 닉네임
    private String followerNickname; // 팔로워 닉네임
    private String profileImageUrl;  // 프로필 이미지
}
