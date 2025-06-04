package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "MADMOVIE_WORLD_CUP_RESULT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MadmovieWorldCupResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "world_cup_seq")
    @SequenceGenerator(name = "world_cup_seq", sequenceName = "SEQ_WORLD_CUP_NO", allocationSize = 1)
    @Column(name = "WORLD_CUP_NO")
    private Integer worldCupNo;

    // 외래키 연관관계 매핑 - 우승한 비디오
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VIDEO_NO", nullable = false)
    private Video video;

    // 외래키 연관관계 매핑 - 월드컵 참여자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "WIN_DATE", nullable = false)
    private Date winDate;
}