package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.repository.FileRepository;
import com.gaeko.gamecut.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {
    private final String baseUploadDir = System.getProperty("user.dir") + "/upload";
    private final FileService fileService;
    private final PhotoService photoService;
    private final FileRepository fileRepository;
    LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    private static final Set<String> ALLOWED_VIDEO_TYPES = Set.of(
            "video/mp4", "video/avi", "video/mkv", "video/mov"
    );

    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024;   // 10MB
    private static final long MAX_VIDEO_SIZE = 200 * 1024 * 1024; // 200MB

    public FileDTO store(MultipartFile file) throws IOException {
        String mimeType = file.getContentType();

        if (mimeType == null || mimeType.isEmpty() ||
                (!ALLOWED_IMAGE_TYPES.contains(mimeType) && !ALLOWED_VIDEO_TYPES.contains(mimeType))) {
            throw new IllegalArgumentException("허용되지 않은 파일 형식입니다.");
        }

        long fileSize = file.getSize();

        if (ALLOWED_IMAGE_TYPES.contains(mimeType) && fileSize > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("이미지 파일은 10MB 이하만 업로드할 수 있습니다.");
        }
        if (ALLOWED_VIDEO_TYPES.contains(mimeType) && fileSize > MAX_VIDEO_SIZE) {
            throw new IllegalArgumentException("동영상 파일은 200MB 이하만 업로드할 수 있습니다.");
        }

        // 날짜별 경로 구성
        LocalDate today = LocalDate.now();
        String datePath = String.format(
                "%d/%02d/%02d",
                today.getYear(), today.getMonthValue(), today.getDayOfMonth()
        );

        // 타입별 분류
        String type = ALLOWED_IMAGE_TYPES.contains(mimeType) ? "img" : "video";

        // 전체 저장 경로
        String fullPath = baseUploadDir + "/" + type + "/" + datePath;
        File dir = new File(fullPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 파일명 생성
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "_" + originalFileName;

        String realPath = fullPath + "/" + fileName;
        String fileUrl = "/upload/" + type + "/" + datePath + "/" + fileName;

        // 파일 저장
        File dest = new File(realPath);
        file.transferTo(dest);

        // DTO 리턴
        return FileDTO.builder()
                      .uuid(uuid)
                      .fileUrl(fileUrl)
                      .realPath(realPath)
                      .mimeType(mimeType)
                      .uploadTime(new Date())
                      .originalFileName(originalFileName)
                      .build();
    }

    public void thumbnailChange(BoardDTO boardDTO) {
        FileUtil fileUtil = new FileUtil();
        List<String> imageUrls = fileUtil.extractImageUrls(boardDTO.getBoardContent());
        int order = 1;
        for (String imageUrl : imageUrls) {
            int index = imageUrl.indexOf("/upload");
            if (index == -1) continue;
            String purePath = imageUrl.substring(index); // /upload부터 자름
            log.info("purePath : " + purePath);
            FileDTO thisFileDTO = fileService.findByFileUrl(purePath);
            log.info(thisFileDTO.toString());
            if (thisFileDTO != null) {
                photoService.save(boardDTO.getBoardNo(), thisFileDTO.getAttachNo(), order);
                order++;
            }
        }
    }

    public void NotUsedFileDelete() {
        List<String> notUsedRealPaths = fileRepository.findRealPathNotUsedInPhotoAndVideo(oneWeekAgo);
        for (String notUsedRealPath : notUsedRealPaths) {
            File file = new File(notUsedRealPath);
            if (file.exists()) {
                boolean deleted = file.delete();
            }
        }
    }

    public void NotUsedDbDelete() {
        List<Integer> notUserAttachNos = fileRepository.findAttachNoUsedInPhotoAndVideo(oneWeekAgo);
        for (Integer notUserAttachNo : notUserAttachNos) {
            fileRepository.deleteByAttachNo(notUserAttachNo);
        }
    }

    // 2025년 7월 8일 수정됨 - 프로필 사진 삭제를 위한 파일 삭제 메서드 추가
    /**
     * 실제 파일을 물리적으로 삭제합니다.
     *
     * @param realPath 삭제할 파일의 실제 경로
     * @return 삭제 성공 여부
     */
    public boolean deleteFile(String realPath) {
        if (realPath == null || realPath.isEmpty()) {
            log.warn("삭제할 파일 경로가 null이거나 비어있습니다.");
            return false;
        }

        File file = new File(realPath);
        if (!file.exists()) {
            log.warn("삭제하려는 파일이 존재하지 않습니다: {}", realPath);
            return false;
        }

        boolean deleted = file.delete();
        if (deleted) {
            log.info("파일 삭제 성공: {}", realPath);
        } else {
            log.error("파일 삭제 실패: {}", realPath);
        }

        return deleted;
    }
}
