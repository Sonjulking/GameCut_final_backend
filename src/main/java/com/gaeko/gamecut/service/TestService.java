package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.TestDTO;
import com.gaeko.gamecut.entity.Test;
import com.gaeko.gamecut.mapper.TestMapper;
import com.gaeko.gamecut.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public List<TestDTO> findAll() {
        return testRepository.findAll()         // 1단계: DB에서 모든 Test 엔티티 가져옴 (List<Test>)
                             .stream()                       // 2단계: List<Test>를 스트림으로 변환
                             //.map(test->TestMapper.toDTO(test)
                             .map(TestMapper::toDTO)        // 3단계: 각 Test 객체를 TestDTO로 변환
                             .collect(Collectors.toList()); // 4단계: 변환된 결과들을 다시 List<TestDTO>로 수집
    }

    public TestDTO findById(int id) {
        Test test = testRepository.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("해당 ID 없음: " + id));
        return TestMapper.toDTO(test);
    }

    public TestDTO save(TestDTO dto) {
        Test saved = testRepository.save(TestMapper.toEntity(dto));
        return TestMapper.toDTO(saved);
    }

    public void delete(int id) {
        testRepository.deleteById(id);
    }
}
