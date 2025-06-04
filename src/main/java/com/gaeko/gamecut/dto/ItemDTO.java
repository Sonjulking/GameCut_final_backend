package com.gaeko.gamecut.dto;

import lombok.*;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {
    private Integer itemNo;
	private String itemName;
	private Integer itemPrice;
	private Date itemDeleteDate;
	private Integer attachNo;
}