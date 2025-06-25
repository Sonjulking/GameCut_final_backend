package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.entity.BoardType;
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
                TagByVideoMapper.class,
                TagMapper.class,
                PhotoMapper.class,
                FileMapper.class
        }
)
public interface BoardMapper {

    @Mapping(source = "boardType.boardTypeNo", target = "boardTypeNo")
    BoardDTO toDTO(Board board);

    List<BoardDTO> toDTOs(List<Board> boards);

    @Mapping(target = "boardType", ignore = true)
    Board toEntity(BoardDTO dto);

    List<Board> toEntities(List<BoardDTO> dtos);
}
