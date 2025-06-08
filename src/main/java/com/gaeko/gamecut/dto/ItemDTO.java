package com.gaeko.gamecut.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {

	private Integer itemNo;

	private String itemName;

	private Integer itemPrice;

	private Date itemDeleteDate;

	// 아이템 이미지 파일 (FileDTO로 매핑)
	private FileDTO itemImage;
}
