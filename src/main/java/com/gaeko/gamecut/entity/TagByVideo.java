package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TAG_BY_VIDEO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(TagByVideoId.class)
public class TagByVideo {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VIDEO_NO", nullable = false)
    private Video video;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TAG_NAME", nullable = false)
    private Tag tag;
}

// 복합키 ID 클래스
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
class TagByVideoId implements java.io.Serializable {
    private Integer video;
    private String tag;
}