package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.entity.Photo;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.service.*;
import com.gaeko.gamecut.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    //게시글 상세페이지
    @GetMapping("/detail/{boardNo}")
    public ResponseEntity<BoardDTO> getBoardDetail(@PathVariable int boardNo) {
        System.out.println("컨트롤러 넘어옴");
        try {
            // 게시글 상세조회 및 조회수 증가
            BoardDTO boardDTO = boardService.findByNo(boardNo);
            System.out.println(boardDTO);
            if (boardDTO == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(boardDTO);

        } catch (Exception e) {
            // 로그 출력
            System.err.println("게시글 상세조회 실패: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/listAll")
    public List<BoardDTO> listAll() {
        return boardService.getAll();
    }

    @GetMapping("/list")
    public List<BoardDTO> list() {
        System.out.println(boardService.getAllBoards());
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
            @PathVariable Integer boardNo,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "existingVideoNo", required = false) String existingVideoNo
    ) throws IOException {

        if (boardDTO.getBoardTypeNo() != 3) {
            photoService.deleteByBoardNo(boardDTO);
            fileUploadService.thumbnailChange(boardDTO);
        } else {
            log.info("board attach : " + boardDTO.getVideo());
            int clientExistingVideoNo = 0;
            if (existingVideoNo == null || existingVideoNo.equals("null")) {
                existingVideoNo = null;
            } else {
                clientExistingVideoNo = Integer.parseInt(existingVideoNo);
            }

            if (existingVideoNo != null) {
                log.info("board existingVideoNo : " + existingVideoNo);
                VideoDTO existingVideo = videoService.findByVideoNo(clientExistingVideoNo);
                boardDTO.setVideo(existingVideo);
            } else {
                FileDTO fileDTO = fileUploadService.store(file);
                fileDTO.setUserNo(1);
                fileDTO = fileService.save(fileDTO);

                String mimeType = file.getContentType();
                if (mimeType != null && mimeType.contains("video")) {
                    VideoDTO videoDTO = videoService.save(boardDTO.getBoardNo(), fileDTO.getAttachNo());
                    boardDTO.setVideo(videoDTO);
                }

                if (thumbnail != null && !thumbnail.isEmpty()) {
                    FileDTO thisFileDTO = fileUploadService.store(thumbnail);
                    thisFileDTO.setUserNo(1);
                    thisFileDTO = fileService.save(thisFileDTO);
                    photoService.save(boardDTO.getBoardNo(), thisFileDTO.getAttachNo(), 1);
                }
            }

        }
        boardDTO.setBoardNo(boardNo);
        boardDTO = boardService.save(boardDTO);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/one")
    public List<BoardDTO> one(@RequestBody List<Long> excludeBoardNos) {
        //return boardService.getOneBoard();
        return boardService.getOneBoardExcluding(excludeBoardNos);
    }

    @PostMapping
    public ResponseEntity<?> insertBoard(
            @ModelAttribute BoardDTO boardDTO,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail
    ) throws IOException {
        boardDTO = boardService.save(boardDTO);
        FileUtil fileUtil = new FileUtil();
        FileDTO fileDTO = null;

        //동영상일떄
        if (file != null && !file.isEmpty()) {
            fileDTO = fileUploadService.store(file);
            fileDTO.setUserNo(1);
            fileDTO = fileService.save(fileDTO);

            String mimeType = file.getContentType();
            if (mimeType != null && mimeType.contains("video")) {
                videoService.save(boardDTO.getBoardNo(), fileDTO.getAttachNo());
            }

            if (thumbnail != null && !thumbnail.isEmpty()) {
                FileDTO thisFileDTO = fileUploadService.store(thumbnail);
                thisFileDTO.setUserNo(1);
                thisFileDTO = fileService.save(thisFileDTO);
                photoService.save(boardDTO.getBoardNo(), thisFileDTO.getAttachNo(), 1);
            }

            //게시판일때
        } else {
            fileUploadService.thumbnailChange(boardDTO);
        }
        return ResponseEntity.ok("OK");
    }


    @PostMapping("/img")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
        log.info("이미지 등록 컨트롤러");
        try {
            // 1. 파일 저장 (임시 저장)
            FileDTO fileDTO = fileUploadService.store(image); // 예: /uploads/temp/uuid.jpg
            fileDTO.setUserNo(1); // 필요 시 사용자 번호 설정

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

}
