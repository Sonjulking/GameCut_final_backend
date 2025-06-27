package com.gaeko.gamecut;

import com.gaeko.gamecut.dto.*;
import com.gaeko.gamecut.entity.*;
import com.gaeko.gamecut.mapper.BoardTypeMapper;
import com.gaeko.gamecut.mapper.UserMapper;
import com.gaeko.gamecut.mapper.VideoMapper;
import com.gaeko.gamecut.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class GamecutBackendApplicationTests {
    @Autowired
    private TestService testService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BoardTypeService boardTypeService;


    @Test
    void contextLoads() {
    }

    @Test
    void insertTestData() {
        for (int i = 1; i <= 5; i++) {
            TestDTO dto = TestDTO.builder()
                                 .testId(i)
                                 .testTitle("제목q " + i)
                                 .testName("이름q " + i)
                                 .build();
            testService.save(dto);
        }
    }


    @Test
    void insertUser() {
        UserDTO dto = UserDTO.builder()
                             .userId("test")
                             .userPwd("1234")
                             .userName("테스터고")
                             .userNickname("test")
                             .phone("010-5555-5555")
                             .userPoint(0)
                             .email("test@test.com")
                             .role("ROLE_USER")
                             .isSocial("basic")
                             .build();

        userService.saveUser(dto, null);
    }

    @Test
    void insertFile() {
        FileDTO dto = FileDTO.builder()
                             .userNo(1)
                             .uuid("1real2-2222-uuid")
                             .originalFileName("test.mp4")
                             .fileUrl("/upload/test.mp4")
                             .realPath("c:/upload/test.mp4")
                             .mimeType("video/mp4")
                             .build();
        FileDTO save = fileService.save(dto);
    }

    //처음 실행시 이거 먼저 실행...
    @Test
    void insertBoardType() {
        BoardTypeDTO boardTypeDTO1 = BoardTypeDTO.builder()
                                                 .boardTypeName("자유")
                                                 .build();
        boardTypeService.save(boardTypeDTO1);
        BoardTypeDTO boardTypeDTO2 = BoardTypeDTO.builder()
                                                 .boardTypeName("공략")
                                                 .build();
        boardTypeService.save(boardTypeDTO2);
        BoardTypeDTO boardTypeDTO3 = BoardTypeDTO.builder()
                                                 .boardTypeName("영상")
                                                 .build();
        boardTypeService.save(boardTypeDTO3);
    }

    @Test
    void insertBoard() {
        UserDTO userDTO = userService.findUserByUserNo(1);

        //BoardTypeDTO boardTypeDTO = boardTypeService.findByBoardTypeNo(1);


        BoardDTO boardDTO = BoardDTO.builder()
                                    .boardTypeNo(1)
                                    .boardLike(0)
                                    .boardCount(0)
                                    .user(userDTO)
                                    .boardTitle("hello zito")
                                    .boardContent("hello world")
                                    .build();

        boardService.save(boardDTO, 1);

    }

    @Test
    void insertVideo() {
        FileDTO fileDTO = fileService.findByAttachNo(1);
        log.info("fileDto : {}", fileDTO);

        VideoDTO dto = VideoDTO.builder()
                               .attachFile(fileDTO)
                               .boardNo(1)
                               .build();
        videoService.save(dto);
    }

    @Test
    void insertComment() {
        UserDTO userDTO = userService.findUserByUserNo(1);
        CommentDTO commentDTO = CommentDTO.builder()
                                          .boardNo(1)
                                          .user(userDTO)
                                          .commentContent("hello akali")
                                          .build();
        commentService.save(commentDTO, 1);
    }

}
