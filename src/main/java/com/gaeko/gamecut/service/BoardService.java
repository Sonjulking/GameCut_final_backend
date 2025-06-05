package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.mapper.BoardMapper;
import com.gaeko.gamecut.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public BoardDTO getBoard(Integer boardNo) {
        Board board = boardRepository.findById(boardNo)
                                     .orElseThrow(() -> new RuntimeException("게시글이 없습니다."));
        return boardMapper.toDTO(board);
    }

    public List<BoardDTO> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return boardMapper.toDTOs(boards);
    }
}
