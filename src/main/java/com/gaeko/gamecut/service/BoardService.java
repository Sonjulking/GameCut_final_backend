// 2025-07-03 생성됨
package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.dto.TagDTO;
import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.entity.*;
import com.gaeko.gamecut.mapper.BoardMapper;
import com.gaeko.gamecut.mapper.CommentMapper;
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
import java.util.Map;
import java.util.stream.Collectors;

// 2025-07-03 생성됨
@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;
    private final BoardTypeRepository boardTypeRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final BoardLikeRepository boardLikeRepository;

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

// 2025-07-03 생성됨
    public List<BoardDTO> getAllBoards() {
        List<Board> boards = boardRepository.findRandom5BoardType3NotDeleted();

        // 게시글 번호 리스트 추출
        List<Integer> boardNos = boards.stream()
                .map(Board::getBoardNo)
                .collect(Collectors.toList());

        // 모든 게시글의 상위 5개 댓글을 배치로 조회
        List<Comment> top5Comments = commentRepository.findTop5CommentsByBoardNos(boardNos);
        
        // 게시글별로 댓글 그룹핑
        Map<Integer, List<Comment>> commentsByBoardNo = top5Comments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getBoard().getBoardNo()));

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

        // BoardDTO로 변환하면서 상위 5개 댓글만 설정
        return boards.stream().map(board -> {
            BoardDTO dto = boardMapper.toDTO(board);
            List<Comment> boardComments = commentsByBoardNo.getOrDefault(board.getBoardNo(), List.of());
            dto.setComments(boardComments.stream()
                    .map(commentMapper::toDTO)
                    .collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

// 2025-07-03 생성됨
    public List<BoardDTO> getOneBoard() {
        List<Board> boards = boardRepository.findRandomOneBoard(PageRequest.of(0, 1));

        // 게시글 번호 리스트 추출
        List<Integer> boardNos = boards.stream()
                .map(Board::getBoardNo)
                .collect(Collectors.toList());

        // 모든 게시글의 상위 5개 댓글을 배치로 조회
        List<Comment> top5Comments = commentRepository.findTop5CommentsByBoardNos(boardNos);
        
        // 게시글별로 댓글 그룹핑
        Map<Integer, List<Comment>> commentsByBoardNo = top5Comments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getBoard().getBoardNo()));

        for (Board board : boards) {
            Video video = board.getVideo();
            if (video != null) {
                if (video.getAttachFile() != null) {
                    video.getAttachFile().getFileUrl(); // Lazy 로딩 유도
                }
                video.getBoard(); // boardNo도 채워줌
            }
        }

        // BoardDTO로 변환하면서 상위 5개 댓글만 설정
        return boards.stream().map(board -> {
            BoardDTO dto = boardMapper.toDTO(board);
            List<Comment> boardComments = commentsByBoardNo.getOrDefault(board.getBoardNo(), List.of());
            dto.setComments(boardComments.stream()
                    .map(commentMapper::toDTO)
                    .collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

// 2025-07-03 생성됨
    public Page<BoardDTO> getAll(int page, int size, Integer boardTypeNo) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardNo")); // 최신순 정렬
        Page<Board> boardPage;
        if (boardTypeNo == null) {
            boardPage = boardRepository.findByBoardDeleteDateIsNull(pageable);
        } else {
            BoardType type = boardTypeRepository.findBoardTypeByBoardTypeNo(boardTypeNo);
            boardPage = boardRepository.findByBoardDeleteDateIsNullAndBoardType(pageable, type);
        }

        // 게시글 번호 리스트 추출
        List<Integer> boardNos = boardPage.getContent().stream()
                .map(Board::getBoardNo)
                .collect(Collectors.toList());

        // 모든 게시글의 상위 5개 댓글을 배치로 조회
        List<Comment> top5Comments = commentRepository.findTop5CommentsByBoardNos(boardNos);
        
        // 게시글별로 댓글 그룹핑
        Map<Integer, List<Comment>> commentsByBoardNo = top5Comments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getBoard().getBoardNo()));

        return boardPage.map(board -> {
            BoardDTO dto = boardMapper.toDTO(board);
            // 해당 게시글의 상위 5개 댓글만 설정
            List<Comment> boardComments = commentsByBoardNo.getOrDefault(board.getBoardNo(), List.of());
            dto.setComments(boardComments.stream()
                    .map(commentMapper::toDTO)
                    .collect(Collectors.toList()));
            return dto;
        });
    }

// 2025-07-03 생성됨
    public List<BoardDTO> getOneBoardExcluding(List<Long> excludeBoardNos) {
        log.info(excludeBoardNos.toString());
        List<Board> boards;
        if (excludeBoardNos == null || excludeBoardNos.isEmpty()) {
            boards = boardRepository.findRandomOneBoard((PageRequest.of(0, 1)));
        } else {
            boards = boardRepository.findRandomOneBoardExclude(excludeBoardNos, (PageRequest.of(0, 1)));
        }

        // 게시글 번호 리스트 추출
        List<Integer> boardNos = boards.stream()
                .map(Board::getBoardNo)
                .collect(Collectors.toList());

        // 모든 게시글의 상위 5개 댓글을 배치로 조회
        List<Comment> top5Comments = commentRepository.findTop5CommentsByBoardNos(boardNos);
        
        // 게시글별로 댓글 그룹핑
        Map<Integer, List<Comment>> commentsByBoardNo = top5Comments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getBoard().getBoardNo()));

        for (Board board : boards) {
            Video video = board.getVideo();
            if (video != null) {
                if (video.getAttachFile() != null) {
                    video.getAttachFile().getFileUrl(); // Lazy 로딩 유도
                }
                video.getBoard(); // boardNo도 채워줌
            }
        }

        // BoardDTO로 변환하면서 상위 5개 댓글만 설정
        return boards.stream().map(board -> {
            BoardDTO dto = boardMapper.toDTO(board);
            List<Comment> boardComments = commentsByBoardNo.getOrDefault(board.getBoardNo(), List.of());
            dto.setComments(boardComments.stream()
                    .map(commentMapper::toDTO)
                    .collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

// 2025-07-03 생성됨
    public BoardDTO findByNo(int boardNo) {
        Board b = boardRepository.findBoardByBoardNo(boardNo);

        return boardMapper.toDTO(b);
    }

    // 게시글 상세 조회 시 모든 댓글 포함
    @Transactional(readOnly = true)
    public BoardDTO findByNoWithAllComments(int boardNo) {
        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        
        // 댓글들의 Lazy Loading을 강제로 초기화
        board.getComments().size(); // 댓글 리스트를 실제로 로드
        
        // 각 댓글의 좋아요 수도 확실히 로드되도록 함
        board.getComments().forEach(comment -> {
            comment.getCommentLike(); // 좋아요 수 강제 로드
            if (comment.getUser() != null) {
                comment.getUser().getUserNickname(); // 유저 정보도 로드
            }
        });
        
        BoardDTO boardDTO = boardMapper.toDTO(board);
        return boardDTO;
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

  @Transactional
  public void deleteBoard(Integer boardNo) {
    boardRepository.deleteByBoardNo(boardNo);
  }

public void boardLike(Integer userNo, Integer boardNo) {
    // User와 Board 엔티티 조회
    User user = userRepository.findById(userNo)
                             .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));
    
    Board board = boardRepository.findById(boardNo)
                                 .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
    
    // BoardLike 엔티티 생성 및 저장
    BoardLike boardLike = BoardLike.builder()
                                   .user(user)
                                   .board(board)
                                   .build();

    board.setBoardLike(board.getBoardLike()+1);
    boardRepository.save(board);
    boardLikeRepository.save(boardLike);
}

public void boardUnlike(Integer userNo, Integer boardNo) {
    // User와 Board 엔티티 조회
    User user = userRepository.findById(userNo)
                             .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));
    
    Board board = boardRepository.findById(boardNo)
                                 .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
    board.setBoardLike(board.getBoardLike()-1);
    boardRepository.save(board);
    boardLikeRepository.deleteByUserNoAndBoardNo(userNo, boardNo);
}

public Boolean isLike(Integer userNo, Integer boardNo) {
    return boardLikeRepository.existsByUserNoAndBoardNo(userNo, boardNo);
}


}
