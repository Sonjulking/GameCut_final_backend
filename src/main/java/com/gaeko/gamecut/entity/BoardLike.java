package com.gaeko.gamecut.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BOARD_LIKE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(BoardLikeId.class)
public class BoardLike {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO", nullable = false)
    private User user; // 좋아요 누른 사용자

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_NO", nullable = false)
    private Board board; // 좋아요 받은 게시글
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
class BoardLikeId implements java.io.Serializable {
    private Integer user;
    private Integer board;
}