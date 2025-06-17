package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.Comment;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.mapper.CommentMapper;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.repository.CommentRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public CommentDTO save(CommentDTO commentDTO) {
        Comment comment = commentMapper.toEntity(commentDTO);

        Board board = boardRepository.findBoardByBoardNo(commentDTO.getBoardNo());
        comment.setBoard(board);


        if (comment.getUser() != null) {
            User user = comment.getUser();
            comment.setUser(user);
        } else {
            User user = userRepository.findUserByUserNo(1);
            comment.setUser(user);
        }

        comment = commentRepository.save(comment);
        return commentMapper.toDTO(comment);
    }


    
}
