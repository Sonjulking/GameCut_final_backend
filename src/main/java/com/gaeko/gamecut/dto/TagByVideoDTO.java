package com.gaeko.gamecut.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagByVideoDTO {
    private Integer videoNo;
    private String tagName;
}
