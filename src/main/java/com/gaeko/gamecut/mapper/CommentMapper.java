package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.entity.Comment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "boardNo", source = "board.boardNo")
    CommentDTO toDTO(Comment comment);

    @Mapping(target = "board", ignore = true)
        // 이거 꼭 필요
    Comment toEntity(CommentDTO dto);
}
