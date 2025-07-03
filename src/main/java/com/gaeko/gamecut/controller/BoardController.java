package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.entity.Photo;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.repository.TagRepository;
import com.gaeko.gamecut.service.*;
import com.gaeko.gamecut.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final FileService fileService;
    private final FileUploadService fileUploadService;
    private final VideoService videoService;
    private final PhotoService photoService;
    private final UserService userService;
    private final TagService tagService;
    private final TagByVideoService tagByVideoService;

    // 2025-07-03 생성됨
    //게시글 상세페이지
    @GetMapping("/detail/{boardNo}")
    public ResponseEntity<BoardDTO> getBoardDetail(@PathVariable int boardNo) {
        try {
            // 게시글 상세조회 및 조회수 증가
            BoardDTO boardDTO = boardService.findByNo(boardNo);
            if (boardDTO == null) {
                return ResponseEntity.notFound().build();
            }
            boardDTO.setBoardCount(boardDTO.getBoardCount() + 1);
            boardService.save(boardDTO, boardDTO.getUser().getUserNo());
            return ResponseEntity.ok(boardDTO);

        } catch (Exception e) {
            // 로그 출력
            System.err.println("게시글 상세조회 실패: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/listAll")
    public Page<BoardDTO> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "boardTypeNo", required = false) Integer boardTypeNo
    ) {
        log.info("boardTypeNo : " + boardTypeNo);
        return boardService.getAll(page, size, boardTypeNo); // 전체 Page 객체 반환
    }

    @GetMapping("/list")
    public List<BoardDTO> list() {
        return boardService.getAllBoards();
    }

    @GetMapping("/{boardNo}")
    public BoardDTO getBoardById(@PathVariable Integer boardNo) {
        return boardService.getBoard(boardNo);
    }

    //수정기능
    @PutMapping("/{boardNo}")
    public ResponseEntity<?> updateBoardById(
            @ModelAttribute BoardDTO boardDTO,
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Integer boardNo,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "existingVideoNo", required = false) String existingVideoNo,
            @RequestParam(value = "videoTags", required = false) List<String> videoTags
    ) throws IOException {

        Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());
        VideoDTO videoDTO = null;
        if (boardDTO.getBoardTypeNo() != 3) {
            photoService.deleteByBoardNo(boardDTO);
            fileUploadService.thumbnailChange(boardDTO);
        } else {
            log.warn("board attach : " + boardDTO.getVideo());
            int clientExistingVideoNo = 0;
            if (existingVideoNo == null || existingVideoNo.equals("null")) {
                existingVideoNo = null;
            } else {
                clientExistingVideoNo = Integer.parseInt(existingVideoNo);
            }

            if (existingVideoNo != null) {
                log.warn("board existingVideoNo : " + existingVideoNo);
                videoDTO = videoService.findByVideoNo(clientExistingVideoNo);
                boardDTO.setVideo(videoDTO);
            } else {
                FileDTO fileDTO = fileUploadService.store(file);
                fileDTO.setUserNo(userNo);
                fileDTO = fileService.save(fileDTO);

                String mimeType = file.getContentType();
                if (mimeType != null && mimeType.contains("video")) {
                    videoDTO = videoService.save(boardDTO.getBoardNo(), fileDTO.getAttachNo());
                    boardDTO.setVideo(videoDTO);
                }


            }
            //
            if (thumbnail != null && !thumbnail.isEmpty()) {
                FileDTO thisFileDTO = fileUploadService.store(thumbnail);
                thisFileDTO.setUserNo(userNo);
                thisFileDTO = fileService.save(thisFileDTO);
                photoService.deleteByBoardNo(boardDTO);
                photoService.save(boardDTO.getBoardNo(), thisFileDTO.getAttachNo(), 1);
            }


            if (videoTags != null && !videoTags.isEmpty()) {
                Integer vId = boardDTO.getVideo().getVideoNo();
                tagByVideoService.deleteByVideo(vId);  // 한 번만 전체 삭제
                System.out.println("videoTags = " + videoTags);
                log.info("videoTags = " + videoTags);
                log.info("videoNo = " + videoDTO.getVideoNo());
                for (String videoTag : videoTags) {
                    String cleanTag = videoTag.startsWith("#") ? videoTag.substring(1) : videoTag;
                    cleanTag = cleanTag.trim();
                    tagService.insert(cleanTag);
                    tagByVideoService.insertOnly(cleanTag, videoDTO.getVideoNo());
                }
            }

        }
        boardDTO.setBoardNo(boardNo);
        boardDTO = boardService.save(boardDTO, userNo);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/one")
    public List<BoardDTO> one(@RequestBody List<Long> excludeBoardNos) {
        //return boardService.getOneBoard();
        return boardService.getOneBoardExcluding(excludeBoardNos);
    }

    @PostMapping
    public ResponseEntity<?> insertBoard(
            @AuthenticationPrincipal UserDetails loginUser,
            @ModelAttribute BoardDTO boardDTO,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "videoTags", required = false) List<String> videoTags

    ) throws IOException {
        Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());

        log.info("username : " + loginUser.getUsername());

        boardDTO = boardService.save(boardDTO, userNo);
        FileUtil fileUtil = new FileUtil();
        FileDTO fileDTO = null;

        //동영상일떄
        if (file != null && !file.isEmpty()) {

            fileDTO = fileUploadService.store(file);
            fileDTO.setUserNo(userNo);
            fileDTO = fileService.save(fileDTO);

            VideoDTO videoDTO = null;
            String mimeType = file.getContentType();
            if (mimeType != null && mimeType.contains("video")) {
                videoDTO = videoService.save(boardDTO.getBoardNo(), fileDTO.getAttachNo());
            }

            if (thumbnail != null && !thumbnail.isEmpty()) {
                FileDTO thisFileDTO = fileUploadService.store(thumbnail);
                thisFileDTO.setUserNo(userNo);
                thisFileDTO = fileService.save(thisFileDTO);
                photoService.save(boardDTO.getBoardNo(), thisFileDTO.getAttachNo(), 1);
            }
            if (videoTags != null && !videoTags.isEmpty()) {
                System.out.println("videoTags = " + videoTags);
                log.info("videoTags = " + videoTags);
                log.info("videoNo = " + videoDTO.getVideoNo());
                for (String videoTag : videoTags) {
                    log.warn("태그 삽입 : " + videoTag);
                    String cleanTag = videoTag.startsWith("#") ? videoTag.substring(1) : videoTag;
                    cleanTag = cleanTag.trim();

                    tagService.insert(cleanTag);
                    tagByVideoService.insertOnly(cleanTag, videoDTO.getVideoNo());
                }
            }

            //게시판일때
        } else {
            fileUploadService.thumbnailChange(boardDTO);
        }
        return ResponseEntity.ok("OK");
    }


    @PostMapping("/img")
    public ResponseEntity<?> uploadImage(
            @RequestParam("image") MultipartFile image,
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        log.info("이미지 등록 컨트롤러");
        Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());

        try {
            // 1. 파일 저장 (임시 저장)
            FileDTO fileDTO = fileUploadService.store(image); // 예: /uploads/temp/uuid.jpg
            fileDTO.setUserNo(userNo); // 필요 시 사용자 번호 설정

            // 2. DB 저장
            fileDTO = fileService.save(fileDTO);

            // 3. 이미지 URL 반환
            String imageUrl = fileDTO.getFileUrl(); // 예: /uploads/temp/uuid.jpg

            log.info("imgUrl : {}", imageUrl);
            return ResponseEntity.ok().body(
                    java.util.Collections.singletonMap("imageUrl", imageUrl)
            );
        } catch (Exception e) {
            log.error("이미지 업로드 실패", e);
            return ResponseEntity.status(500).body("이미지 업로드 실패");
        }
    }
    @DeleteMapping("/{boardNo}")
    public void deleteBoard(@PathVariable Integer boardNo) {
        boardService.deleteBoard(boardNo);
    }

    @PostMapping("/like/{boardNo}")
    public void boardLike(@PathVariable Integer boardNo, @AuthenticationPrincipal UserDetails loginUser){
        Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());
        boardService.boardLike(userNo, boardNo);
    }

    @PostMapping("/unlike/{boardNo}")
    public void boardUnlike(@PathVariable Integer boardNo, @AuthenticationPrincipal UserDetails loginUser){
        Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());
        boardService.boardUnlike(userNo, boardNo);
    }

    @PostMapping("/isLike/{boardNo}")
    public Boolean isLike(@PathVariable Integer boardNo, @AuthenticationPrincipal UserDetails loginUser) {
        Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());
        System.out.println("좋아요체크들어옴");
        return boardService.isLike(userNo, boardNo);
    }
}
