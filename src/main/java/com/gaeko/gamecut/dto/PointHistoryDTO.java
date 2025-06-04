package com.gaeko.gamecut.dto;

import java.util.Date;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointHistoryDTO {
    private Integer pointHistoryNo;
	private Integer userNo;
	private Date pointDate;
	private Integer pointAmount;
	private String pointSource;
}
