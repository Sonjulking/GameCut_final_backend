package com.gaeko.gamecut.dto;

import java.util.Date;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Integer commentNo;
    private Integer boardNo;
    private Integer userNo;
    private Integer parentCommentNo;
    private String commentContent;
    private Date commentCreateDate;
    private Date commentDeleteDate;

    // 추가 권장 필드들 (화면 표시용)
    private String userNickname;        // 댓글 작성자 닉네임
    private String profileImageUrl;     // 작성자 프로필 이미지
    private boolean likedByCurrentUser; // 현재 사용자의 좋아요 여부
    private Integer likeCount;          // 댓글 좋아요 수
    private boolean isReply;            // 대댓글 여부
}
