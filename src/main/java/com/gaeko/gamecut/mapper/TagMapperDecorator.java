package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.TagDTO;
import com.gaeko.gamecut.entity.Tag;
import com.gaeko.gamecut.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class TagMapperDecorator implements TagMapper {

    private final TagMapper delegate;
    private final TagRepository tagRepository;

    @Autowired
    public TagMapperDecorator(TagMapper delegate, TagRepository tagRepository) {
        this.delegate = delegate;
        this.tagRepository = tagRepository;
    }

    @Override
    public TagDTO toDTO(Tag tag) {
        TagDTO dto = delegate.toDTO(tag);

        // 레포지토리 통해 fileUrl 세팅
        String fileUrl = tagRepository.findFileUrlByTagName(tag.getTagName());
        dto.setFileUrl(fileUrl);

        return dto;
    }

    @Override
    public Tag toEntity(TagDTO dto) {
        return delegate.toEntity(dto);
    }
}
