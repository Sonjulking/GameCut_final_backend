package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public List<BoardDTO> list() {
        return boardService.getAllBoards();
    }

    @GetMapping("/one")
    public BoardDTO board() {
        return boardService.getBoard(1);
    }

    @PostMapping
    public BoardDTO insertBoard(@ModelAttribute BoardDTO boardDTO) {
        return boardService.save(boardDTO);
    }
}
