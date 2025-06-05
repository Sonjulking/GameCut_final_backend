package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.BoardTypeDTO;
import com.gaeko.gamecut.entity.BoardType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardTypeMapper {
    BoardTypeDTO toDTO(BoardType boardType);
    BoardType toEntity(BoardTypeDTO dto);
}
