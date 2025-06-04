package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "GUESS_THE_RANK_HISTORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuessTheRankHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gtr_history_seq")
    @SequenceGenerator(name = "gtr_history_seq", sequenceName = "SEQ_GTR_HISTORY_NO", allocationSize = 1)
    @Column(name = "GTR_HISTORY_NO")
    private Integer gtrHistoryNo;

    // 외래키 연관관계 매핑 - 랭크 맞추기 게임
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GTR_NO", nullable = false)
    private GuessTheRank guessTheRank;

    // 외래키 연관관계 매핑 - 게임 참여자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO", nullable = false)
    private User user;

    @Column(name = "IS_CORRECT", length = 1)
    private String isCorrect;

    @CreationTimestamp
    @Column(name = "SOLVE_DATE", nullable = false)
    private Date solveDate;
}