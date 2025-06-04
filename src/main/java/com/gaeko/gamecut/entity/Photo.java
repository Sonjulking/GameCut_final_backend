package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PHOTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_seq")
    @SequenceGenerator(name = "photo_seq", sequenceName = "SEQ_PHOTO_NO", allocationSize = 1)
    @Column(name = "PHOTO_NO", nullable = false)
    private Integer photoNo;

    // 외래키 연관관계 매핑 - 첨부파일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ATTACH_NO", nullable = false)
    private File attachFile;

    // 외래키 연관관계 매핑 - 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_NO", nullable = false)
    private Board board;

    @Column(name = "PHOTO_ORDER", nullable = false)
    @Builder.Default
    private Integer photoOrder = 1;
}
