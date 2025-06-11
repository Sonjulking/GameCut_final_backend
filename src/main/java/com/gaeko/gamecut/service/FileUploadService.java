package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.FileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final String uploadDir = System.getProperty("user.dir") + "/upload";

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    private static final Set<String> ALLOWED_VIDEO_TYPES = Set.of(
            "video/mp4", "video/avi", "video/mkv"
    );
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final long MAX_VIDEO_SIZE = 200 * 1024 * 1024; // 200MB

    public FileDTO store(MultipartFile file) throws IOException {
        String mimeType = file.getContentType();

        if (mimeType == null || mimeType.isEmpty() || (!ALLOWED_IMAGE_TYPES.contains(mimeType) && !ALLOWED_VIDEO_TYPES.contains(mimeType))) {
            throw new IllegalArgumentException("허용되지 않은 파일 형식입니다.");
        }

        long fileSize = file.getSize();

        if (ALLOWED_IMAGE_TYPES.contains(mimeType) && fileSize > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("이미지 파일은 10MB 이하만 업로드할 수 있습니다.");
        }
        if (ALLOWED_VIDEO_TYPES.contains(mimeType) && fileSize > MAX_VIDEO_SIZE) {
            throw new IllegalArgumentException("동영상 파일은 200MB 이하만 업로드할 수 있습니다.");
        }


        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "_" + originalFileName;
        String realPath = uploadDir + "/" + fileName;
        String fileUrl = "/upload/" + fileName;

        File dest = new File(realPath);
        file.transferTo(dest);

        return FileDTO.builder()
                      .uuid(uuid)
                      .fileUrl(fileUrl)
                      .realPath(realPath)
                      .mimeType(file.getContentType())
                      .uploadTime(new Date())
                      .originalFileName(originalFileName)
                      .build();
    }
}
