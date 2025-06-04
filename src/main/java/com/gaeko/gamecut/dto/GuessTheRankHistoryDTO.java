package com.gaeko.gamecut.dto;

import java.util.Date;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuessTheRankHistoryDTO {
    private Integer gtrHistoryNo;
    private Integer gtrNo;
    private Integer userNo;
    private String isCorrect;
    private Date solveDate;

    private String userNickname;    // 플레이어 닉네임
    private String videoTitle;      // 비디오 제목  
    private String correctTier;     // 정답 티어
}
