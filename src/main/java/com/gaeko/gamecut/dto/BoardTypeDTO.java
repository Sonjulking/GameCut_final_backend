package com.gaeko.gamecut.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardTypeDTO {
    private Integer boardTypeNo;
    private String boardTypeName;
}
