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
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
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

    public void commentLike(Integer commentNo, Integer userNo) {
        // User와 Board 엔티티 조회
        User user = userRepository.findById(userNo)
                                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));
        
        Comment comment = commentRepository.findById(commentNo)
                                    .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        
        // BoardLike 엔티티 생성 및 저장
        CommentLike commentLike = CommentLike.builder()
                                    .user(user)
                                    .comment(comment)
                                    .build();

        
    }



    
}
