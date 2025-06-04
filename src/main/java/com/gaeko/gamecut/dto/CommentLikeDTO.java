package com.gaeko.gamecut.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentLikeDTO {
    private Integer commentNo;
	private Integer userNo;
}
