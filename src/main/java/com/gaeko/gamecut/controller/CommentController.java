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
import org.springframework.web.bind.annotation.*;


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

// 2025-07-03 생성됨
    // 댓글 좋아요 취소
    @PostMapping("/unlike/{commentNo}")
    public void commentUnlike(@PathVariable Integer commentNo, @AuthenticationPrincipal UserDetails loginUser) {
        Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());
        // 댓글 좋아요 취소 로직
        commentService.commentUnlike(commentNo, userNo);
    }

    @PostMapping("/isLike/{commentNo}") 
    public boolean isLike(@PathVariable Integer commentNo, @AuthenticationPrincipal UserDetails loginUser) {
        Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());
        return commentService.isCommentLiked(commentNo, userNo);
    }

    // 특정 게시글의 댓글 페이징 조회
    @GetMapping("/board/{boardNo}")
    public ResponseEntity<List<CommentDTO>> getCommentsByBoard(
            @PathVariable Integer boardNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<CommentDTO> comments = commentService.getCommentsByBoardNo(boardNo, page, size);
        return ResponseEntity.ok(comments);
    }

    // 특정 게시글의 댓글 총 개수 조회
    @GetMapping("/board/{boardNo}/count")
    public ResponseEntity<Long> getCommentCountByBoard(@PathVariable Integer boardNo) {
        Long count = commentService.getCommentCountByBoardNo(boardNo);
        return ResponseEntity.ok(count);
    }

}
