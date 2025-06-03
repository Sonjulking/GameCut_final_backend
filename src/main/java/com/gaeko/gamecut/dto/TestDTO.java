package com.gaeko.gamecut.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestDTO {
    private int testId;
    private String testTitle;
    private String testName;
}
