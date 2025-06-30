package com.gaeko.gamecut.dto;

import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class VideoRankingDTO {
    private Integer videoNo;
    private Long winCount;
    private Long totalRuns;
    private Double winRate;       // 0.0 ~ 1.0
    private String videoRealPath; // DB 에 저장된 realPath
}
