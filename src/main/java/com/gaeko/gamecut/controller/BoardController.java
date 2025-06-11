package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.service.BoardService;
import com.gaeko.gamecut.service.FileService;
import com.gaeko.gamecut.service.FileUploadService;
import com.gaeko.gamecut.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final FileService fileService;
    private final FileUploadService fileUploadService;
    private final VideoService videoService;

    @GetMapping("/list")
    public List<BoardDTO> list() {
        return boardService.getAllBoards();
    }

    @GetMapping("/one")
    public BoardDTO board() {
        return boardService.getBoard(1);
    }

    @PostMapping
    public ResponseEntity<?> insertBoard(
            @ModelAttribute BoardDTO boardDTO,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        FileDTO fileDTO = fileUploadService.store(file);
        fileDTO.setUserNo(1);
        fileDTO = fileService.save(fileDTO);
        boardDTO = boardService.save(boardDTO);
        String mimeType = file.getContentType();
        /*if (mimeType.contains("image")) {

        }*/

        if (mimeType.contains("video")) {
            videoService.save(boardDTO.getBoardNo(), fileDTO.getAttachNo());
        }

        return ResponseEntity.ok("OK");
    }
}
