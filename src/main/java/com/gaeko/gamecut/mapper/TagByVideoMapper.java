package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.TagByVideoDTO;
import com.gaeko.gamecut.entity.Tag;
import com.gaeko.gamecut.entity.TagByVideo;
import com.gaeko.gamecut.entity.Video;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {TagMapper.class})
public interface TagByVideoMapper {

    @Mapping(source = "videoNo", target = "video")
    @Mapping(source = "tag", target = "tag")
    TagByVideo toEntity(TagByVideoDTO dto);

    @Mapping(source = "video.videoNo", target = "videoNo")
    @Mapping(source = "tag.tagName", target = "tag")
    TagByVideoDTO toDTO(TagByVideo entity);

    // ⭐ Integer → Video 변환
    default Video map(Integer videoNo) {
        return videoNo == null ? null : Video.builder().videoNo(videoNo).build();
    }

    // ⭐ String → Tag 변환
    default Tag map(String tagName) {
        return tagName == null ? null : Tag.builder().tagName(tagName).build();
    }
}
