package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.PhotoDTO;
import com.gaeko.gamecut.entity.Photo;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = { FileMapper.class }
)
public interface PhotoMapper {

    @Mappings({
            @Mapping(target = "boardNo", source = "board.boardNo") // Photo.board.getBoardNo() → boardNo
            // attachFile은 FileMapper가 알아서 File → FileDTO로 변환
            // photoOrder도 필드명이 동일하므로 자동 매핑
    })
    PhotoDTO toDTO(Photo photo);

    @Mappings({
            // PhotoDTO → Photo 엔티티로 매핑할 때
            // attachFile(FileDTO→File)와 board(BoardDTO→Board) 매핑은 서비스 로직에서 엔티티를 찾아 세팅해야 하므로 무시
            @Mapping(target = "attachFile", ignore = true),
            @Mapping(target = "board",      ignore = true)
    })
    Photo toEntity(PhotoDTO dto);
}
