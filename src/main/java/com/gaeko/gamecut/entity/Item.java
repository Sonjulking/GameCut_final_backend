package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "ITEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
    @SequenceGenerator(name = "item_seq", sequenceName = "SEQ_ITEM_NO", allocationSize = 1)
    @Column(name = "ITEM_NO")
    private Integer itemNo;

    @Column(name = "ITEM_NAME", nullable = false, length = 30)
    private String itemName;

    @Column(name = "ITEM_PRICE", nullable = false)
    private Integer itemPrice;

    @Column(name = "ITEM_DELETE_DATE")
    private Date itemDeleteDate;

    // 외래키 연관관계 매핑 - 아이템 이미지 파일 (ERD에서 추가 확인)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ATTACH_NO", nullable = false)
    private File itemImage;
}