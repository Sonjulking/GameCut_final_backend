package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.PhotoDTO;
import com.gaeko.gamecut.entity.Photo;
import org.mapstruct.*;


@Mapper(
        componentModel = "spring",
        uses = { FileMapper.class }
)
public interface PhotoMapper {

    PhotoDTO toDTO(Photo photo);

    Photo toEntity(PhotoDTO dto);
}
