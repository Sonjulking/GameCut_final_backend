package com.gaeko.gamecut.mapper;


import com.gaeko.gamecut.dto.TagDTO;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.entity.Tag;
import org.mapstruct.*;


import com.gaeko.gamecut.dto.TagDTO;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.entity.Tag;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { FileMapper.class })
public interface TagMapper {

    @Mapping(source = "file", target = "file")
    TagDTO toDTO(Tag tag);

    @Mapping(source = "file", target = "file")
    Tag toEntity(TagDTO dto);
}



