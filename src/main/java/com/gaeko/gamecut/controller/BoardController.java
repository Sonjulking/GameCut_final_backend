package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.entity.Photo;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/listAll")
    public List<BoardDTO> listAll() {
        return boardService.getAll();
    }

    @GetMapping("/list")
    public List<BoardDTO> list() {
        System.out.println(boardService.getAllBoards());
        return boardService.getAllBoards();
    }

    @PostMapping("/one")
    public List<BoardDTO> one(@RequestBody List<Long> excludeBoardNos) {
        //return boardService.getOneBoard();
        return boardService.getOneBoardExcluding(excludeBoardNos);
    }

    @PostMapping
    public ResponseEntity<?> insertBoard(
            @ModelAttribute BoardDTO boardDTO,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {
        FileDTO fileDTO = null;

        if (file != null && !file.isEmpty()) {
            fileDTO = fileUploadService.store(file);
            fileDTO.setUserNo(1);
            fileDTO = fileService.save(fileDTO);
            boardDTO = boardService.save(boardDTO);


            String mimeType = file.getContentType();
            if (mimeType != null && mimeType.contains("video")) {
                videoService.save(boardDTO.getBoardNo(), fileDTO.getAttachNo());
            }
            if (mimeType != null && mimeType.contains("image")) {
                photoService.save(boardDTO.getBoardNo(), fileDTO.getAttachNo(), 1);
            }
        } else {
            boardDTO = boardService.save(boardDTO);

        }



        return ResponseEntity.ok("OK");
    }

}
