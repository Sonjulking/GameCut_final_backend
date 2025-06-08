package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.dto.BoardDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                UserMapper.class,
                CommentMapper.class,
                VideoMapper.class,
                PhotoMapper.class
        }
)
public interface BoardMapper {

    BoardDTO toDTO(Board board);

    List<BoardDTO> toDTOs(List<Board> boards);

    Board toEntity(BoardDTO dto);

    List<Board> toEntities(List<BoardDTO> dtos);
}
