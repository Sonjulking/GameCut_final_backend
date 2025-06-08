package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.entity.Video;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {FileMapper.class}
)
public interface VideoMapper {


    VideoDTO toDTO(Video video);


    Video toEntity(VideoDTO dto);
}
