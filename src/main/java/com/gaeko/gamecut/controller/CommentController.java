package com.gaeko.gamecut.controller;


import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.service.CommentService;
import com.gaeko.gamecut.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
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
    
    // 여러 댓글의 좋아요 상태 일괄 조회
    @PostMapping("/isLike/batch")
    public ResponseEntity<Map<Integer, Boolean>> isLikeBatch(
            @RequestBody List<Integer> commentNos, 
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        if (loginUser == null) {
            // 비로그인 사용자는 모든 댓글에 대해 false 반환
            Map<Integer, Boolean> result = commentNos.stream()
                .collect(Collectors.toMap(commentNo -> commentNo, commentNo -> false));
            return ResponseEntity.ok(result);
        }
        
        Integer userNo = userService.userNoFindByUserName(loginUser.getUsername());
        Map<Integer, Boolean> likeStatus = commentService.getCommentsLikeStatus(commentNos, userNo);
        return ResponseEntity.ok(likeStatus);
    }

    // 특정 게시글의 댓글 페이징 조회 (좋아요 상태 포함)
    @GetMapping("/board/{boardNo}")
    public ResponseEntity<List<CommentDTO>> getCommentsByBoard(
            @PathVariable Integer boardNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        log.info("댓글 조회 API 호출 - boardNo: {}, page: {}, size: {}, loginUser: {}", 
                 boardNo, page, size, loginUser != null ? loginUser.getUsername() : "비로그인");
        
        Integer currentUserNo = null;
        if (loginUser != null) {
            currentUserNo = userService.userNoFindByUserName(loginUser.getUsername());
            log.info("현재 사용자 번호: {}", currentUserNo);
        }
        
        List<CommentDTO> comments = commentService.getCommentsByBoardNoWithLikeStatus(
            boardNo, page, size, currentUserNo);
        
        log.info("조회된 댓글 수: {}", comments.size());
        if (!comments.isEmpty()) {
            CommentDTO firstComment = comments.get(0);
            log.info("첫 번째 댓글 좋아요 상태: {}", firstComment.getIsLikedByCurrentUser());
        }
        
        return ResponseEntity.ok(comments);
    }

    // 특정 게시글의 댓글 총 개수 조회
    @GetMapping("/board/{boardNo}/count")
    public ResponseEntity<Long> getCommentCountByBoard(@PathVariable Integer boardNo) {
        Long count = commentService.getCommentCountByBoardNo(boardNo);
        return ResponseEntity.ok(count);
    }

    // 2025년 7월 10일 추가됨 - 특정 게시글의 모든 댓글 조회 (pages용)
    @GetMapping("/board/{boardNo}/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByBoard(
            @PathVariable Integer boardNo,
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        log.info("모든 댓글 조회 API 호출 - boardNo: {}, loginUser: {}", 
                 boardNo, loginUser != null ? loginUser.getUsername() : "비로그인");
        
        Integer currentUserNo = null;
        if (loginUser != null) {
            currentUserNo = userService.userNoFindByUserName(loginUser.getUsername());
        }
        
        List<CommentDTO> allComments = commentService.getAllCommentsByBoardNoWithLikeStatus(
            boardNo, currentUserNo);
        
        log.info("조회된 전체 댓글 수: {}", allComments.size());
        
        return ResponseEntity.ok(allComments);
    }

    // 2025-07-14 수정됨 - 삭제된 댓글도 포함하여 모든 댓글 조회 (pages용)
    @GetMapping("/board/{boardNo}/all-including-deleted")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByBoardIncludingDeleted(
            @PathVariable Integer boardNo,
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        log.info("삭제된 댓글 포함 모든 댓글 조회 API 호출 - boardNo: {}, loginUser: {}", 
                 boardNo, loginUser != null ? loginUser.getUsername() : "비로그인");
        
        Integer currentUserNo = null;
        if (loginUser != null) {
            currentUserNo = userService.userNoFindByUserName(loginUser.getUsername());
        }
        
        List<CommentDTO> allComments = commentService.getAllCommentsByBoardNoIncludingDeletedWithLikeStatus(
            boardNo, currentUserNo);
        
        log.info("조회된 전체 댓글 수 (삭제된 댓글 포함): {}", allComments.size());
        
        return ResponseEntity.ok(allComments);
    }

    // 2025-07-14 수정됨 - 삭제된 댓글도 포함한 총 개수 조회 (pages용)
    @GetMapping("/board/{boardNo}/count-including-deleted")
    public ResponseEntity<Long> getAllCommentCountByBoardIncludingDeleted(@PathVariable Integer boardNo) {
        Long count = commentService.getAllCommentCountByBoardNo(boardNo);
        return ResponseEntity.ok(count);
    }

}
