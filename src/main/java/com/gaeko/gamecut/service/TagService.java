package com.gaeko.gamecut.service;

import com.gaeko.gamecut.entity.Tag;
import com.gaeko.gamecut.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    public void insert(String tagName) {
            if (!tagRepository.existsById(tagName)) { //존재할때 제외함
                Tag tag = new Tag();
                tag.setTagName(tagName);
                tag.setFile(null);
                tagRepository.save(tag);
            }
        }
}
