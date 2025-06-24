package com.gaeko.gamecut.mapper;


import com.gaeko.gamecut.dto.TagDTO;
import com.gaeko.gamecut.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",   config = TagMapperConfig.class)
public interface TagMapper {


    @Mapping(target = "fileUrl", source = "file", qualifiedByName = "fileToUrl")
    TagDTO toDTO(Tag tag);

    // fileUrl은 엔티티로 매핑하지 않으므로 무시 처리
    @Mapping(target = "file", ignore = true)
    Tag toEntity(TagDTO dto);

    @Named("fileToUrl")
    static String mapFileToUrl(com.gaeko.gamecut.entity.File file) {
        return file != null ? file.getFileUrl() : null;
    }
}



