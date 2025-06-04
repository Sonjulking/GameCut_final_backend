package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "REPORT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_seq")
    @SequenceGenerator(name = "report_seq", sequenceName = "SEQ_REPORT_NO", allocationSize = 1)
    @Column(name = "REPORT_NO")
    private Integer reportNo;

    // 외래키 연관관계 매핑 - 신고한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO", nullable = false)
    private User user;

    // 외래키 연관관계 매핑 - 신고당한 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_NO", nullable = false)
    private Board board;

    @Column(name = "REPORT_CONTENT", length = 50)
    private String reportContent;

    @Column(name = "REPORT_TYPE", nullable = false, length = 50)
    private String reportType;

    @CreationTimestamp
    @Column(name = "REPORT_DATE", nullable = false)
    private Date reportDate;
}