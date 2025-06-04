
package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "POINT_HISTORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "point_history_seq")
    @SequenceGenerator(name = "point_history_seq", sequenceName = "SEQ_POINT_HISTORY_NO", allocationSize = 1)
    @Column(name = "POINT_HISTORY_NO")
    private Integer pointHistoryNo;

    // 외래키 연관관계 매핑 - 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "POINT_DATE", nullable = false)
    private Date pointDate;

    @Column(name = "POINT_AMOUNT", nullable = false)
    private Integer pointAmount;

    @Column(name = "POINT_SOURCE", nullable = false, length = 50)
    private String pointSource;
}