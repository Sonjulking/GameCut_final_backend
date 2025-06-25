package com.gaeko.gamecut.entity;

import lombok.*;

// 복합키 ID 클래스
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TagByVideoId implements java.io.Serializable {
    private Integer video;
    private String tag;
}