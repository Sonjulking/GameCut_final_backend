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
    private UserDTO user;

    // 대댓글 구조를 위한 부모 댓글 번호만 저장 (없으면 null)
    private CommentDTO parentComment;
    
    private String boardTitle; // ✨ 게시글 제목 (화면에서 제목 출력용)

    private Integer commentLike;
    private String commentContent;
    private Date commentCreateDate;
    private Date commentDeleteDate;
    
    // 현재 로그인한 사용자가 이 댓글에 좋아요를 눌렀는지 여부
    private Boolean isLikedByCurrentUser;
}
