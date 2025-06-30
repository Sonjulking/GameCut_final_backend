package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.dto.TagDTO;
import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.entity.*;
import com.gaeko.gamecut.mapper.BoardMapper;
import com.gaeko.gamecut.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final VideoRepository videoRepository;
    private final BoardTypeRepository boardTypeRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public BoardDTO save(BoardDTO boardDTO, Integer userNo) {


        if (boardDTO.getBoardCount() == null) {
            boardDTO.setBoardCount(0);
        }
        if (boardDTO.getBoardLike() == null) {
            boardDTO.setBoardLike(0);
        }
        //수정 시 기존 createDate 유지
        if (boardDTO.getBoardNo() != null) {
            Board existing = boardRepository.findById(boardDTO.getBoardNo())
                                            .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
            boardDTO.setBoardCreateDate(existing.getBoardCreateDate());
        } else {
            // 신규 작성 시 현재 시각 설정
            boardDTO.setBoardCreateDate(new Date());
        }
        Board board = boardMapper.toEntity(boardDTO);
        BoardType boardType = boardTypeRepository.findById(boardDTO.getBoardTypeNo()).orElse(null);


        User user = userRepository.findUserByUserNo(userNo);
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
                for (TagByVideo tagByVideo : video.getTagByVideoList()) {
                    Tag tag = tagByVideo.getTag();
                    String fileUrl = tagRepository.findFileUrlByTagName(tag.getTagName());
                    TagDTO tagDTO = new TagDTO();
                    tagDTO.setFileUrl(fileUrl);
                }

                if (video.getAttachFile() != null) {
                    video.getAttachFile().getFileUrl();
                }

                video.getBoard();
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

    public Page<BoardDTO> getAll(int page, int size, Integer boardTypeNo) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardNo")); // 최신순 정렬
        Page<Board> boardPage;
        if (boardTypeNo == null) {
            boardPage = boardRepository.findAll(pageable);
        } else {
            BoardType type = boardTypeRepository.findBoardTypeByBoardTypeNo(boardTypeNo);
            boardPage = boardRepository.findAllByBoardType(pageable, type);

        }

        return boardPage.map(boardMapper::toDTO); // Page<Board> → Page<BoardDTO>
    }

    public List<BoardDTO> getOneBoardExcluding(List<Long> excludeBoardNos) {
        log.info(excludeBoardNos.toString());
        List<Board> boards;
        if (excludeBoardNos == null || excludeBoardNos.isEmpty()) {
            boards = boardRepository.findRandomOneBoard((PageRequest.of(0, 1)));
        } else {
            boards = boardRepository.findRandomOneBoardExclude(excludeBoardNos, (PageRequest.of(0, 1)));
        }


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

    public BoardDTO findByNo(int boardNo) {
        Board b = boardRepository.findBoardByBoardNo(boardNo);

        return boardMapper.toDTO(b);
    }

    /** 모든 영상 게시물 가져오기 **/
  @Transactional(readOnly = true)
  public List<VideoDTO> findAllVideoBoards() {
    return boardRepository.findByBoardType_BoardTypeNo(3).stream()
      .filter(b -> b.getVideo() != null && b.getVideo().getAttachFile() != null)
      .map(b -> {
        VideoDTO dto = new VideoDTO();
        dto.setBoardNo(b.getBoardNo());
        // realPath 에서 /upload/** 이하만 잘라서 담아둔다
        String rp = b.getVideo().getAttachFile().getRealPath();
        int idx = rp.indexOf("/upload/");
        dto.setUrl(idx >= 0 ? rp.substring(idx) : "");
        return dto;
      })
      .collect(Collectors.toList());
  }

}
