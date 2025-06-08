package com.gaeko.gamecut.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {
    // ATTACH_NO
    private Integer attachNo;

    // File 업로드한 사용자(User) 식별자만 저장
    //private UserDTO user;
    private Integer userNo;

    // 고유 식별자(UUID)
    private String uuid;

    // 외부에서 접근할 수 있는 URL
    private String fileUrl;

    // 실제 서버에 저장된 경로
    private String realPath;

    // MIME 타입 (ex: "image/png", "video/mp4" 등)
    private String mimeType;

    // 업로드 시각
    private Date uploadTime;

    // 원본 파일 이름
    private String originalFileName;

}
