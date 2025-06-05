package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board/list")
    public List<BoardDTO> list() {
        return boardService.getAllBoards();
    }

    @GetMapping("/board/one")
    public BoardDTO board() {
        return boardService.getBoard(1);
    }
}
