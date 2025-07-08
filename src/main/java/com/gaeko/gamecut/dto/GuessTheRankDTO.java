package com.gaeko.gamecut.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuessTheRankDTO {
    private Integer gtrNo;
    private Integer videoNo;
    private VideoDTO video;  // 2025년 7월 8일 수정됨 - Video 정보 포함
    private String tier;
    private String gameType; // 2025년 7월 8일 수정됨 - 게임 종류 추가
}
