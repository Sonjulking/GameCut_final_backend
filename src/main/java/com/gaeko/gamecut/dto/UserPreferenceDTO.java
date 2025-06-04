package com.gaeko.gamecut.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferenceDTO {
    private Integer userNo;
    private String tagName;
    private Integer tagCount;
}
