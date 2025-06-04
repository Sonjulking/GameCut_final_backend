package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "MESSAGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", sequenceName = "SEQ_MESSAGE_NO", allocationSize = 1)
    @Column(name = "MESSAGE_NO")
    private Integer messageNo;

    // 외래키 연관관계 매핑 - 메시지 발신자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEND_USER_NO", nullable = false)
    private User sendUser;

    // 외래키 연관관계 매핑 - 메시지 수신자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVE_USER_NO", nullable = false)
    private User receiveUser;

    @Column(name = "MESSAGE_CONTENT", length = 200)
    private String messageContent;

    @CreationTimestamp
    @Column(name = "MESSAGE_DATE", nullable = false)
    private Date messageDate;

    @Column(name = "MESSAGE_DELETE_DATE")
    private Date messageDeleteDate;
}