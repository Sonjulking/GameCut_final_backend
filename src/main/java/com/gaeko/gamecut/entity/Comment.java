package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "COMMENT_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    @SequenceGenerator(name = "comment_seq", sequenceName = "SEQ_COMMENT_NO", allocationSize = 1)
    @Column(name = "COMMENT_NO")
    private Integer commentNo;

    // 외래키 연관관계 매핑 - 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_NO", nullable = false)
    private Board board;

    // 외래키 연관관계 매핑 - 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO", nullable = false)
    private User user;

    // 외래키 연관관계 매핑 - 부모 댓글 (대댓글용, 선택적)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_COMMENT_NO", referencedColumnName = "COMMENT_NO")
    private Comment parentComment;

    @Column(name = "COMMENT_LIKE", nullable = false)
    @Builder.Default
    private Integer commentLike = 0;

    @Column(name = "COMMENT_CONTENT", nullable = false, length = 200)
    private String commentContent;

    @CreationTimestamp
    @Column(name = "COMMENT_CREATE_DATE", nullable = false)
    private Date commentCreateDate;

    @Column(name = "COMMENT_DELETE_DATE")
    private Date commentDeleteDate;
}