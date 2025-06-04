package com.gaeko.gamecut.dto;

import java.util.Date;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {
    private Integer attachNo;
	private Integer userNo;
	private String uuid;
	private String fileUrl;
	private String realPath;
	private String mimeType;
	private Date uploadTime;
	private String originalFileName;

	private String uploaderNickname; // 업로더 닉네임
	private Long fileSize;           // 파일 크기	
}
