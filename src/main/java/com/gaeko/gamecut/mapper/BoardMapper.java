package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.entity.Board;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                UserMapper.class,
                BoardTypeMapper.class,
                CommentMapper.class,
                PhotoMapper.class,
                VideoMapper.class
        }
)
public interface BoardMapper {

    // Board → BoardDTO
    BoardDTO toDTO(Board board);

    // Board 목록 → BoardDTO 목록
    List<BoardDTO> toDTOs(List<Board> boards);

    // (필요 시) BoardDTO → Board
    Board toEntity(BoardDTO dto);
}
