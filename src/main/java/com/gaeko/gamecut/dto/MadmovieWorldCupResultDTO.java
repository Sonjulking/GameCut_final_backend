package com.gaeko.gamecut.dto;

import java.util.Date;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MadmovieWorldCupResultDTO {
    private Integer worldCupNo;
    private Integer videoNo;
    private Integer userNo;
    private Date winDate;
}
