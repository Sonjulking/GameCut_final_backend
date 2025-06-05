package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BOARD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq")
    @SequenceGenerator(name = "board_seq", sequenceName = "SEQ_BOARD_NO", allocationSize = 1)
    @Column(name = "BOARD_NO")
    private Integer boardNo;

    // 외래키 연관관계 매핑 - 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO", nullable = false)
    private User user;

    // 외래키 연관관계 매핑 - 게시판 타입
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_TYPE_NO", nullable = false)
    private BoardType boardType;

    @Column(name = "BOARD_CONTENT", nullable = false, length = 200)
    private String boardContent;

    @Column(name = "BOARD_TITLE", nullable = false, length = 200)
    private String boardTitle;

    @Column(name = "BOARD_COUNT", nullable = false)
    @Builder.Default
    private Integer boardCount = 0;

    @Column(name = "BOARD_LIKE", nullable = false)
    @Builder.Default
    private Integer boardLike = 0;

    @CreationTimestamp
    @Column(name = "BOARD_CREATE_DATE", nullable = false)
    private Date boardCreateDate;

    @Column(name = "BOARD_DELETE_DATE")
    private Date boardDeleteDate;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Video video;

    @OneToMany(mappedBy = "board")
    private List<Photo> photos;
}
