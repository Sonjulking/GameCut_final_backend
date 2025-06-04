package com.gaeko.gamecut.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuessTheRankDTO {
    private Integer gtrNo;
    private Integer videoNo;
    private String tier;
}
