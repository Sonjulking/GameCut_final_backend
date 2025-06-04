package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "USER_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "SEQ_USER_NO", allocationSize = 1)
    @Column(name = "USER_NO")
    private Integer userNo;

    @Column(name = "USER_ID", nullable = false, length = 30, unique = true)
    private String userId;

    @Column(name = "USER_PWD", nullable = false, length = 30)
    private String userPwd;

    @Column(name = "USER_NAME", nullable = false, length = 30)
    private String userName;

    @Column(name = "USER_NICKNAME", nullable = false, length = 30, unique = true)
    private String userNickname;

    @Column(name = "PHONE", nullable = false, length = 13)
    private String phone;

    @Column(name = "EMAIL", nullable = false, length = 30)
    private String email;

    @CreationTimestamp
    @Column(name = "USER_CREATE_DATE", nullable = false)
    private Date userCreateDate;

    @Column(name = "USER_DELETE_DATE")
    private Date userDeleteDate;

    @Column(name = "IS_SOCIAL", nullable = false, length = 20)
    private String isSocial;

    @Column(name = "ROLE", nullable = false, length = 30)
    private String role;

    @Column(name = "USER_POINT", nullable = false)
    @Builder.Default
    private Integer userPoint = 1000;

    // 외래키 연관관계 매핑 - 착용중인 아이템
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_NO")
    private Item item;

    // 외래키 연관관계 매핑 - 프로필 사진
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHOTO_NO")
    private Photo photo;
}