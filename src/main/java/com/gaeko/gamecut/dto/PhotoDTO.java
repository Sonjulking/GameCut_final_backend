package com.gaeko.gamecut.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoDTO {
    private Integer photoNo;

    // File 정보를 DTO로 담습니다.
    private FileDTO attachFile;

    // 게시글(Board) 번호만 저장
    private Integer boardNo;

    // 사진 순서를 나타내는 필드
    @Builder.Default
    private Integer photoOrder = 1;
}
