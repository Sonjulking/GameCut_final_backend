package com.gaeko.gamecut.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockDTO {
    private Integer blockerNo;
    private Integer blockedNo;
}
