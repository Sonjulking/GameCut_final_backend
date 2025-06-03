package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.TestDTO;
import com.gaeko.gamecut.entity.Test;
import lombok.Builder;

@Builder
public class TestMapper {

    public static TestDTO toDTO(Test entity) {
        if (entity == null) return null;

        return TestDTO.builder()
                      .testId(entity.getTestId())
                      .testTitle(entity.getTestTitle())
                      .testName(entity.getTestName())
                      .build();
    }

    public static Test toEntity(TestDTO dto) {
        if (dto == null) return null;

        return Test.builder()
                   .testId(dto.getTestId())
                   .testTitle(dto.getTestTitle())
                   .testName(dto.getTestName())
                   .build();
    }
}
