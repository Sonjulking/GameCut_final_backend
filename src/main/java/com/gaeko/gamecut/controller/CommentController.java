package com.gaeko.gamecut.controller;


import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> saveComment(@RequestBody CommentDTO commentDTO) {
        //임시
        log.info("댓글" + commentDTO.toString());

        return ResponseEntity.ok(commentService.save(commentDTO));
    }

}
