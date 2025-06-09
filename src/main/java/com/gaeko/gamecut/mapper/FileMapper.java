package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(target = "userNo", source = "user.userNo")
    FileDTO toDTO(File entity);

    @Mapping(target = "user", ignore = true)
    File toEntity(FileDTO dto);
}
