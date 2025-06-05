package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.entity.File;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mappings({
            @Mapping(target = "userNo", source = "user.userNo") // File.user.getUserNo() → userNo
            // 나머지 필드(uuid, fileUrl, realPath, mimeType, uploadTime, originalFileName)는 필드명이 동일해 자동 매핑
    })
    FileDTO toDTO(File file);

    @Mappings({
            // FileDTO → File 엔티티 매핑 시,
            // user(UserDTO→User) 매핑은 서비스 로직에서 직접 User 엔티티를 찾아 세팅해야 하므로 무시
            @Mapping(target = "user", ignore = true)
    })
    File toEntity(FileDTO dto);
}
