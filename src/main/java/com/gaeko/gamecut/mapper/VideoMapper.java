package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.entity.Video;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {FileMapper.class})
public interface VideoMapper {

    @Mapping(source = "board.boardNo", target = "boardNo")
    @Mapping(source = "attachFile", target = "attachFile")
        // FileMapper 통해 DTO 변환
    VideoDTO toDTO(Video video);

    @Mapping(source = "boardNo", target = "board.boardNo") // Entity 재구성 시
    @Mapping(source = "attachFile", target = "attachFile")
    Video toEntity(VideoDTO dto);
}

