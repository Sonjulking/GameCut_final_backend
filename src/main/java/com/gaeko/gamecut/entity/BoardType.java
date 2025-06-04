package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BOARD_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_type_seq")
    @SequenceGenerator(name = "board_type_seq", sequenceName = "SEQ_BOARD_TYPE_NO", allocationSize = 1)
    @Column(name = "BOARD_TYPE_NO")
    private Integer boardTypeNo;

    @Column(name = "BOARD_TYPE_NAME", nullable = false, length = 50)
    private String boardTypeName;
}