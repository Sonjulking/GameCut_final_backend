package com.gaeko.gamecut.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "FILE_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_seq")
    @SequenceGenerator(name = "file_seq", sequenceName = "SEQ_ATTACH_NO", allocationSize = 1)
    @Column(name = "ATTACH_NO")
    private Integer attachNo;

    @ManyToOne
    @JoinColumn(name = "USER_NO", nullable = false)
    private User user;

    @Column(name = "UUID", nullable = false, length = 200, unique = true)
    private String uuid;

    @Column(name = "FILE_URL", nullable = false, length = 300)
    private String fileUrl;

    @Column(name = "REAL_PATH", nullable = false, length = 300)
    private String realPath;

    @Column(name = "MIME_TYPE", nullable = false, length = 50)
    private String mimeType;

    @CreationTimestamp
    @Column(name = "UPLOAD_TIME", nullable = false)
    private Date uploadTime;

    @Column(name = "ORIGINAL_FILE_NAME", nullable = false, length = 100)
    private String originalFileName;
}