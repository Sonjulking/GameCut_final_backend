package com.gaeko.gamecut;

import com.gaeko.gamecut.dto.TestDTO;
import com.gaeko.gamecut.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GamecutBackendApplicationTests {
    @Autowired
    private TestService testService;

    @Test
    void contextLoads() {
    }

    @Test
    void insertTestData() {
        for (int i = 1; i <= 5; i++) {
            TestDTO dto = TestDTO.builder()
                                 .testId(i)
                                 .testTitle("제목 " + i)
                                 .testName("이름 " + i)
                                 .build();
            testService.save(dto);
        }
    }

}
