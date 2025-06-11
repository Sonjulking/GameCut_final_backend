package com.gaeko.gamecut.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {
    private Integer boardNo;

    // 작성자 정보: UserDTO를 사용하거나, 아이디만 필요하다면 Integer userNo로 변경
    private UserDTO user;

    // 게시판 타입 정보: BoardTypeDTO를 사용하거나, 아이디만 필요하다면 Integer boardTypeNo로 변경
    //private BoardTypeDTO boardType;
    private Integer boardTypeNo;

    private String boardContent;
    private String boardTitle;

    // 조회수, 좋아요 수는 기본값이 0으로 세팅되므로, DTO 생성 시에도 기본값을 0으로 놓을 수 있습니다.
    private Integer boardCount;

    private Integer boardLike;

    private Date boardCreateDate;
    private Date boardDeleteDate;

    // 댓글 목록 (연관된 CommentDTO 리스트)
    private List<CommentDTO> comments;

    // 단일 비디오 (연관된 VideoDTO)
    private VideoDTO video;

    // 사진 목록 (연관된 PhotoDTO 리스트)
    private List<PhotoDTO> photos;
}
