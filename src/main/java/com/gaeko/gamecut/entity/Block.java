package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_BLOCK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(BlockId.class)
public class Block {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BLOCKER_NO", nullable = false)
    private User blocker; // 차단한 사용자

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BLOCKED_NO", nullable = false)
    private User blocked; // 차단당한 사용자
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
class BlockId implements java.io.Serializable {
    private Integer blocker;
    private Integer blocked;
}