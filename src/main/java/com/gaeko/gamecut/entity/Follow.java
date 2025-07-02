
package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FOLLOW")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(FollowId.class)
public class Follow {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLLOWEE_NO", nullable = false)
    private User followee; // 팔로우당하는 사람 (팔로잉 대상)

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLLOWER_NO", nullable = false)
    private User follower; // 팔로우하는 사람 (팔로워)
}

/*
// 복합키 ID 클래스
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
class FollowId implements java.io.Serializable {
    private Integer followee;
    private Integer follower;
}
*/