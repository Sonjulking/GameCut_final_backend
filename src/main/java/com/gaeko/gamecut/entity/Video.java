package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VIDEO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_seq")
    @SequenceGenerator(name = "video_seq", sequenceName = "SEQ_VIDEO_NO", allocationSize = 1)
    @Column(name = "VIDEO_NO")
    private Integer videoNo;

    // 외래키 연관관계 매핑 - 첨부파일
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ATTACH_NO", nullable = false, unique = false)
    private File attachFile;

    // 외래키 연관관계 매핑 - 게시글 (ERD에서 추가 확인)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_NO", nullable = false, unique = false)
    private Board board;

    @OneToMany(mappedBy = "video")
    private List<TagByVideo> tagByVideoList;

}