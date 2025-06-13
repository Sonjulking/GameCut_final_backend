package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.BoardType;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.entity.Video;
import com.gaeko.gamecut.mapper.BoardMapper;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.repository.BoardTypeRepository;
import com.gaeko.gamecut.repository.UserRepository;
import com.gaeko.gamecut.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final VideoRepository videoRepository;
    private final BoardTypeRepository boardTypeRepository;
    private final UserRepository userRepository;

    public BoardDTO save(BoardDTO boardDTO) {
        if (boardDTO.getBoardCount() == null) {
            boardDTO.setBoardCount(0);
        }
        if (boardDTO.getBoardLike() == null) {
            boardDTO.setBoardLike(0);
        }
        Board board = boardMapper.toEntity(boardDTO);
        BoardType boardType = boardTypeRepository.findById(boardDTO.getBoardTypeNo()).orElse(null);
        //TODO : 로그인 기능 구현 후 나중에 클라이언트 데이터 받아오기
        User user = userRepository.findUserByUserNo(1);
        board.setUser(user);
        board.setBoardType(boardType);
        board = boardRepository.save(board);
        return boardMapper.toDTO(board);
    }

    public BoardDTO getBoard(Integer boardNo) {
        Board board = boardRepository.findById(boardNo)
                                     .orElseThrow(() -> new RuntimeException("게시글이 없습니다."));
        return boardMapper.toDTO(board);
    }

    public List<BoardDTO> getAllBoards() {
        List<Board> boards = boardRepository.findRandom5BoardType3NotDeleted();

        for (Board board : boards) {
            Video video = board.getVideo();
            if (video != null) {
                if (video.getAttachFile() != null) {
                    video.getAttachFile().getFileUrl(); // Lazy 로딩 유도
                }
                video.getBoard(); // boardNo도 채워줌
            }
        }

        return boardMapper.toDTOs(boards);
    }

    public List<BoardDTO> getOneBoard() {
        List<Board> boards = boardRepository.findRandomOneBoard(PageRequest.of(0, 1));

        for (Board board : boards) {
            Video video = board.getVideo();
            if (video != null) {
                if (video.getAttachFile() != null) {
                    video.getAttachFile().getFileUrl(); // Lazy 로딩 유도
                }
                video.getBoard(); // boardNo도 채워줌
            }
        }

        return boardMapper.toDTOs(boards);
    }


}
