package com.gaeko.gamecut.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Integer commentNo;

    // 연관된 게시글 번호만 저장
    private Integer boardNo;

    // 댓글 작성자(유저) 번호만 저장
    private Integer userNo;

    // 대댓글 구조를 위한 부모 댓글 번호만 저장 (없으면 null)
    private Integer parentCommentNo;

    private String commentContent;
    private Date commentCreateDate;
    private Date commentDeleteDate;
}
