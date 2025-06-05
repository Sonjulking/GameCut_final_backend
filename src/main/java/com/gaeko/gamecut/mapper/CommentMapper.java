package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.entity.Comment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mappings({
            @Mapping(target = "boardNo", source = "board.boardNo"),         // board.user.getBoardNo() → boardNo
            @Mapping(target = "userNo", source = "user.userNo"),          // user.getUserNo() → userNo
            @Mapping(target = "parentCommentNo", source = "parentComment.commentNo") // 대댓글인 경우 parentComment.getCommentNo() → parentCommentNo
    })
    CommentDTO toDTO(Comment comment);

    @Mappings({
            // CommentDTO → Comment 엔티티로 매핑할 땐,
            // board, user, parentComment 필드는 서비스 로직에서 엔티티를 직접 조회 후 세팅해야 하므로 무시합니다.
            @Mapping(target = "board", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "parentComment", ignore = true)
    })
    Comment toEntity(CommentDTO dto);
}
