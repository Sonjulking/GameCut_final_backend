package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.entity.Video;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {FileMapper.class}
)
public interface VideoMapper {

    @Mappings({
            @Mapping(target = "boardNo", source = "board.boardNo") // Video.board.getBoardNo() → boardNo
            // attachFile 필드는 FileMapper가 알아서 File → FileDTO로 변환
    })
    VideoDTO toDTO(Video video);

    @Mappings({
            // VideoDTO → Video 엔티티로 매핑할 때
            // attachFile(FileDTO→File)와 board(BoardDTO→Board) 매핑은 서비스 로직에서 엔티티를 찾아 세팅해야 하므로 무시
            @Mapping(target = "attachFile", ignore = true),
            @Mapping(target = "board", ignore = true)
    })
    Video toEntity(VideoDTO dto);
}
