package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TAG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @Column(name = "TAG_NAME", length = 20)
    private String tagName;

    // 외래키 연관관계 매핑 - 태그 이미지 파일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ATTACH_NO", nullable = true)
    private File file;
}