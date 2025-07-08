// 2025년 7월 8일 수정됨 - GuessTheRank DTO + Mapper 패턴 적용
package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.GuessTheRankDTO;
import com.gaeko.gamecut.entity.GuessTheRank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VideoMapper.class})
public interface GuessTheRankMapper {

    @Mapping(source = "video.videoNo", target = "videoNo")
    @Mapping(source = "video", target = "video")
    GuessTheRankDTO toDTO(GuessTheRank entity);

    @Mapping(source = "videoNo", target = "video.videoNo")
    @Mapping(target = "video", ignore = true) // 별도로 설정할 예정
    GuessTheRank toEntity(GuessTheRankDTO dto);

    List<GuessTheRankDTO> toDTOs(List<GuessTheRank> entities);
    List<GuessTheRank> toEntities(List<GuessTheRankDTO> dtos);
}
