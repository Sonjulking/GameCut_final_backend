package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.entity.Comment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CommentMapper {


    CommentDTO toDTO(Comment comment);


    Comment toEntity(CommentDTO dto);
}
