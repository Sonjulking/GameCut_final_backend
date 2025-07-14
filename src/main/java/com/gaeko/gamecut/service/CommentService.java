// 2025-07-03 생성됨
package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.BoardLike;
import com.gaeko.gamecut.entity.Comment;
import com.gaeko.gamecut.entity.CommentLike;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.mapper.CommentMapper;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.repository.CommentRepository;
import com.gaeko.gamecut.repository.CommentLikeRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public CommentDTO save(CommentDTO commentDTO, Integer userNo) {

        if (commentDTO.getCommentLike() == null) {
            commentDTO.setCommentLike(0);
        }
        Comment comment = commentMapper.toEntity(commentDTO);

        Board board = boardRepository.findBoardByBoardNo(commentDTO.getBoardNo());
        comment.setBoard(board);

        if (comment.getUser() != null) {
            User user = comment.getUser();
            comment.setUser(user);
        } else {
            User user = userRepository.findUserByUserNo(userNo);
            comment.setUser(user);
        }

        comment = commentRepository.save(comment);
        return commentMapper.toDTO(comment);
    }

    //추가
    public List<CommentDTO> getCommentsByUser(Integer userNo) {
        List<Comment> comments = commentRepository.findByUserNo(userNo);
        return comments.stream().map(commentMapper::toDTO).toList();
    }

    @Transactional
    public void deleteComment(Integer commentNo) {
        commentRepository.deleteByCommentNo(commentNo);
    }

    @Transactional
    public void updateComment(Integer commentNo, CommentDTO commentDTO) {
        commentRepository.updateComment(commentNo, commentDTO);
    }

    // 2025-07-03 생성됨
    public void commentLike(Integer commentNo, Integer userNo) {
        // User와 Comment 엔티티 조회
        User user = userRepository.findById(userNo)
                                  .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentNo)
                                           .orElseThrow(() -> new RuntimeException("해당 댓글이 존재하지 않습니다."));

        // 이미 좋아요를 눌렀는지 확인
        if (!commentLikeRepository.existsByUserUserNoAndCommentCommentNo(userNo, commentNo)) {
            // CommentLike 엔티티 생성 및 저장
            CommentLike commentLike = CommentLike.builder()
                                                 .user(user)
                                                 .comment(comment)
                                                 .build();

            commentLikeRepository.save(commentLike);

            // 댓글 좋아요 수 증가
            comment.setCommentLike(comment.getCommentLike() + 1);
            commentRepository.save(comment);
        }
    }

    // 댓글 좋아요 취소
    @Transactional
    public void commentUnlike(Integer commentNo, Integer userNo) {
        // User와 Comment 엔티티 조회
        User user = userRepository.findById(userNo)
                                  .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentNo)
                                           .orElseThrow(() -> new RuntimeException("해당 댓글이 존재하지 않습니다."));

        // 좋아요를 눌렀는지 확인
        if (commentLikeRepository.existsByUserUserNoAndCommentCommentNo(userNo, commentNo)) {
            // 좋아요 삭제
            commentLikeRepository.deleteByUserNoAndCommentNo(userNo, commentNo);

            // 댓글 좋아요 수 감소
            comment.setCommentLike(Math.max(0, comment.getCommentLike() - 1));
            commentRepository.save(comment);
        }
    }

    // 댓글 좋아요 상태 확인
    public Boolean isCommentLiked(Integer commentNo, Integer userNo) {
        return commentLikeRepository.existsByUserUserNoAndCommentCommentNo(userNo, commentNo);
    }
    
    // 여러 댓글의 좋아요 상태 일괄 조회
    public Map<Integer, Boolean> getCommentsLikeStatus(List<Integer> commentNos, Integer userNo) {
        return commentNos.stream()
            .collect(Collectors.toMap(
                commentNo -> commentNo,
                commentNo -> commentLikeRepository.existsByUserUserNoAndCommentCommentNo(userNo, commentNo)
            ));
    }

    // 특정 게시글의 댓글 페이징 조회 (좋아요 상태 포함)
    public List<CommentDTO> getCommentsByBoardNo(Integer boardNo, int page, int size) {
        return getCommentsByBoardNoWithLikeStatus(boardNo, page, size, null);
    }
    
    // 특정 게시글의 댓글 페이징 조회 (좋아요 상태 포함) - 로그인 사용자용
    public List<CommentDTO> getCommentsByBoardNoWithLikeStatus(Integer boardNo, int page, int size, Integer currentUserNo) {
        // 2025-07-14 수정됨 - 삭제된 댓글도 포함하여 전체 댓글 조회 후 페이징 적용
        List<Comment> allComments = commentRepository.findAllCommentsByBoardNoIncludingDeleted(boardNo);

        int start = page * size;
        int end = Math.min(start + size, allComments.size());

        if (start >= allComments.size()) {
            return List.of(); // 빈 리스트 반환
        }

        List<Comment> pagedComments = allComments.subList(start, end);
        
        // 댓글을 DTO로 변환하면서 좋아요 상태 설정
        return pagedComments.stream()
                .map(comment -> {
                    CommentDTO dto = commentMapper.toDTO(comment);
                    
                    // 로그인한 사용자가 있는 경우에만 좋아요 상태 확인
                    if (currentUserNo != null) {
                        boolean isLiked = commentLikeRepository.existsByUserUserNoAndCommentCommentNo(
                            currentUserNo, comment.getCommentNo());
                        dto.setIsLikedByCurrentUser(isLiked);
                    } else {
                        dto.setIsLikedByCurrentUser(false);
                    }
                    
                    return dto;
                })
                .toList();
    }

    // 특정 게시글의 댓글 총 개수 조회
    public Long getCommentCountByBoardNo(Integer boardNo) {
        return commentRepository.countCommentsByBoardNo(boardNo);
    }

    // 2025년 7월 10일 추가됨 - 특정 게시글의 모든 댓글 조회 (페이징 없이)
    public List<CommentDTO> getAllCommentsByBoardNoWithLikeStatus(Integer boardNo, Integer currentUserNo) {
        // 전체 댓글 조회 (페이징 없이)
        List<Comment> allComments = commentRepository.findCommentsByBoardNo(boardNo);
        
        // 댓글을 DTO로 변환하면서 좋아요 상태 설정
        return allComments.stream()
                .map(comment -> {
                    CommentDTO dto = commentMapper.toDTO(comment);
                    
                    // 로그인한 사용자가 있는 경우에만 좋아요 상태 확인
                    if (currentUserNo != null) {
                        boolean isLiked = commentLikeRepository.existsByUserUserNoAndCommentCommentNo(
                            currentUserNo, comment.getCommentNo());
                        dto.setIsLikedByCurrentUser(isLiked);
                    } else {
                        dto.setIsLikedByCurrentUser(false);
                    }
                    
                    return dto;
                })
                .toList();
    }

    // 2025-07-14 수정됨 - 삭제된 댓글도 포함하여 조회하는 새로운 메소드 (pages용)
    public List<CommentDTO> getAllCommentsByBoardNoIncludingDeletedWithLikeStatus(Integer boardNo, Integer currentUserNo) {
        // 삭제된 댓글도 포함하여 전체 댓글 조회
        List<Comment> allComments = commentRepository.findAllCommentsByBoardNoIncludingDeleted(boardNo);
        
        // 댓글을 DTO로 변환하면서 좋아요 상태 설정
        return allComments.stream()
                .map(comment -> {
                    CommentDTO dto = commentMapper.toDTO(comment);
                    
                    // 로그인한 사용자가 있는 경우에만 좋아요 상태 확인
                    if (currentUserNo != null) {
                        boolean isLiked = commentLikeRepository.existsByUserUserNoAndCommentCommentNo(
                            currentUserNo, comment.getCommentNo());
                        dto.setIsLikedByCurrentUser(isLiked);
                    } else {
                        dto.setIsLikedByCurrentUser(false);
                    }
                    
                    return dto;
                })
                .toList();
    }

    // 2025-07-14 수정됨 - 삭제된 댓글도 포함한 총 개수 조회 (pages용)
    public Long getAllCommentCountByBoardNo(Integer boardNo) {
        return commentRepository.countAllCommentsByBoardNo(boardNo);
    }
}