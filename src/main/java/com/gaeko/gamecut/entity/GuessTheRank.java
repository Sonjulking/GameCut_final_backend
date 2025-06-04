
package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GUESS_THE_RANK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuessTheRank {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gtr_seq")
    @SequenceGenerator(name = "gtr_seq", sequenceName = "SEQ_GTR_NO", allocationSize = 1)
    @Column(name = "GTR_NO")
    private Integer gtrNo;

    // 외래키 연관관계 매핑 - 비디오
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VIDEO_NO", nullable = false)
    private Video video;

    @Column(name = "TIER", length = 20)
    private String tier;
}