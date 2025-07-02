package com.gaeko.gamecut.controller;


import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.service.CommentService;
import com.gaeko.gamecut.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    private final UserService userService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> saveComment(
            @RequestBody CommentDTO commentDTO,
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        //임시
        log.info("댓글" + commentDTO.toString());
        Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());
        return ResponseEntity.ok(commentService.save(commentDTO, userNo));
    }
    
    @GetMapping("/my")
    public ResponseEntity<List<CommentDTO>> getMyComments(@AuthenticationPrincipal UserDetails loginUser) {
        Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());
        return ResponseEntity.ok(commentService.getCommentsByUser(userNo));
    }

    @DeleteMapping("/{commentNo}")
    public void deleteComment(@PathVariable Integer commentNo) {
        commentService.deleteComment(commentNo);
    }

    @PutMapping("/{commentNo}")
    public void updateComment(
            @PathVariable Integer commentNo,
            @RequestBody CommentDTO commentDTO,
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        log.info("댓글 수정 - commentNo: " + commentNo + ", 내용: " + commentDTO.toString());

        
        // 댓글 수정 서비스 호출
        commentService.updateComment(commentNo, commentDTO);
        
    }

    // 댓글 좋아요 추가
    @PostMapping("/like/{commentNo}")
    public void commentLike(@PathVariable Integer commentNo, @AuthenticationPrincipal UserDetails loginUser) {
        Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());
        // 댓글 좋아요 로직
        commentService.commentLike(commentNo, userNo);
    }

    // 댓글 좋아요 취소  
    @PostMapping("/unlike/{commentNo}")
    public void commentUnlike(@PathVariable Integer commentNo, @AuthenticationPrincipal UserDetails loginUser) {
        // 댓글 좋아요 취소 로직
    }
   
}
