// 2025년 7월 8일 수정됨 - DTO + Mapper 패턴으로 리팩터링
package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.GuessTheRankDTO;
import com.gaeko.gamecut.entity.GuessTheRank;
import com.gaeko.gamecut.entity.GuessTheRankHistory;
import com.gaeko.gamecut.entity.Video;
import com.gaeko.gamecut.mapper.GuessTheRankMapper;
import com.gaeko.gamecut.repository.GuessTheRankHistoryRepository;
import com.gaeko.gamecut.repository.GuessTheRankRepository;
import com.gaeko.gamecut.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuessTheRankService {

    private final GuessTheRankRepository rankRepo;
    private final GuessTheRankHistoryRepository historyRepo;
    private final VideoRepository videoRepo;
    private final UserService userService;
    private final GuessTheRankMapper guessTheRankMapper; // 2025년 7월 8일 수정됨 - Mapper 추가

    /** 1) 영상 업로드 뒤, tier 정보 저장 */
    // 2025년 7월 8일 수정됨 - DTO 기반으로 변경
    @Transactional
    public GuessTheRankDTO saveTier(Integer videoNo, String tier, String gameType) {
        Video video = videoRepo.findById(videoNo)
            .orElseThrow(() -> new EntityNotFoundException("Video not found: " + videoNo));
        
        GuessTheRankDTO dto = GuessTheRankDTO.builder()
            .videoNo(videoNo)
            .tier(tier)
            .gameType(gameType)
            .build();
            
        GuessTheRank entity = guessTheRankMapper.toEntity(dto);
        entity.setVideo(video); // 비디오 설정
        GuessTheRank saved = rankRepo.save(entity);
        
        return guessTheRankMapper.toDTO(saved);
    }
    
    /** 기존 메서드 호환성을 위해 유지 */
    @Transactional
    public GuessTheRankDTO saveTier(Integer videoNo, String tier) {
        return saveTier(videoNo, tier, null);
    }

    /** 2) 무작위 퀴즈 한 건을 꺼내 옵니다. */
    // 2025년 7월 8일 수정됨 - DTO 반환으로 변경
    @Transactional(readOnly = true)
    public GuessTheRankDTO getRandomQuestion() {
        List<GuessTheRank> list = rankRepo.findRandomOne(PageRequest.of(0, 1));
        GuessTheRank entity = list.stream()
                   .findFirst()
                   .orElseThrow(() -> new EntityNotFoundException("퀴즈 데이터가 없습니다."));
        return guessTheRankMapper.toDTO(entity);
    }

    /** 3) 게임 중 답 제출 → 정답 여부 반환 & 히스토리 저장 */
    // 2025년 7월 8일 수정됨 - 주관식 답안 처리 (대소문자 무시 + 공백 제거)
    @Transactional
    public boolean submitAnswer(Integer gtrNo, String userAnswer) {
        GuessTheRank gtr = rankRepo.findById(gtrNo)
            .orElseThrow(() -> new EntityNotFoundException("GuessTheRank not found: " + gtrNo));
        
        // 주관식 방식: 대소문자 무시 + 공백 제거 후 비교
        String correctTier = gtr.getTier().trim().toLowerCase();
        String userTier = userAnswer.trim().toLowerCase();
        boolean correct = correctTier.equals(userTier);

        GuessTheRankHistory hist = GuessTheRankHistory.builder()
            .guessTheRank(gtr)
            .user(userService.getCurrentUser())
            .isCorrect(correct ? "Y" : "N")
            .solveDate(new Date())
            .build();
        historyRepo.save(hist);

        return correct;
    }

    // 2025년 7월 8일 수정됨 - DTO 리스트 반환으로 변경
    @Transactional(readOnly=true)
    public List<GuessTheRankDTO> getAllByGameType(String gameType) {
        List<GuessTheRank> entities;
        if (gameType == null || gameType.equals("ALL")) {
            entities = rankRepo.findAll();
        } else {
            entities = rankRepo.findByGameType(gameType);
        }
        return guessTheRankMapper.toDTOs(entities);
    }
}
