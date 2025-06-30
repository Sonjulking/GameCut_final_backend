package com.gaeko.gamecut.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDTO {
    private Integer videoNo;

    // attachFile 엔티티의 PK만 담습니다.
    // File 엔티티가 예를 들어 fileNo(PK)를 가지고 있다면,
    // DTO 필드명도 attachNo 또는 fileNo 등으로 맞춰주세요.
    // File 정보를 DTO로 담습니다.
    private FileDTO attachFile;

    // Board 엔티티의 PK만 담습니다.
    private Integer boardNo;

    private List<TagByVideoDTO>  tagByVideoList = new ArrayList<>();

    private String url; 
}
