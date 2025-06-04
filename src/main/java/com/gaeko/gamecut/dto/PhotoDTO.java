package com.gaeko.gamecut.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoDTO {
    private Integer photoNo;
    private Integer attachNo;
    private Integer boardNo;
    private Integer photoOrder;
}
