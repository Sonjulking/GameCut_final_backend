package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_PREFERENCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserPreferenceId.class)
public class UserPreference {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO", nullable = false)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TAG_NAME", nullable = false)
    private Tag tag;

    @Column(name = "TAG_COUNT", nullable = false)
    @Builder.Default
    private Integer tagCount = 0;
}

// 복합키 ID 클래스
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
class UserPreferenceId implements java.io.Serializable {
    private Integer user;
    private String tag;
}