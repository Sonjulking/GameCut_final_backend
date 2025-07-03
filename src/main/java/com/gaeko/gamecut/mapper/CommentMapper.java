package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.entity.Comment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "boardNo", source = "board.boardNo")
    @Mapping(target = "commentLike", source = "commentLike") // 명시적으로 좋아요 수 매핑
    CommentDTO toDTO(Comment comment);

    @Mapping(target = "board", ignore = true)
        // 이거 꼭 필요
    Comment toEntity(CommentDTO dto);
}
