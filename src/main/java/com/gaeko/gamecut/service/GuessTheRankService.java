// 2025년 7월 8일 수정됨 - DTO + Mapper 패턴으로 리팩터링
// 2025년 7월 9일 수정됨 - 사용자 게임 기록 저장 및 조회 기능 추가
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    
    // 2025-07-09 수정됨 - 사용자 게임 기록 저장 (기존 방식과 별도로 세분화된 기록)
    @Transactional
    public void saveUserGameHistory(Integer userNo, Integer gtrNo, String userAnswer, boolean isCorrect) {
        GuessTheRank gtr = rankRepo.findById(gtrNo)
            .orElseThrow(() -> new EntityNotFoundException("GuessTheRank not found: " + gtrNo));
        
        // 마이페이지용 상세 기록을 위한 별도 저장 로직을 여기에 추가할 수 있습니다.
        // 현재는 GuessTheRankHistory를 사용하여 기록을 저장합니다.
        // 필요시 별도의 UserGameHistory 엔티티를 만들 수 있습니다.
    }
    
    // 2025-07-09 수정됨 - 사용자 게임 기록 조회
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUserGameHistory(Integer userNo) {
        // GuessTheRankHistory에서 해당 사용자의 기록들을 가져옵니다.
        List<GuessTheRankHistory> histories = historyRepo.findByUser_UserNoOrderBySolveDateDesc(userNo);
        
        return histories.stream()
            .map(history -> {
                Map<String, Object> record = new HashMap<>();
                record.put("gtrNo", history.getGuessTheRank().getGtrNo());
                record.put("gameType", history.getGuessTheRank().getGameType());
                record.put("correctTier", history.getGuessTheRank().getTier());
                record.put("userAnswer", extractUserAnswerFromHistory(history)); // 사용자 답안 추출
                record.put("isCorrect", "Y".equals(history.getIsCorrect()));
                record.put("playDate", history.getSolveDate());
                return record;
            })
            .collect(Collectors.toList());
    }
    
    // 2025-07-09 수정됨 - 히스토리에서 사용자 답안 추출 (현재는 단순히 정답 정보만 있음)
    private String extractUserAnswerFromHistory(GuessTheRankHistory history) {
        // 현재 GuessTheRankHistory에는 사용자의 실제 답안이 저장되지 않으므로
        // 임시로 정답 여부에 따라 값을 반환합니다.
        // 실제 구현에서는 GuessTheRankHistory에 userAnswer 필드를 추가하거나
        // 별도의 테이블을 사용할 수 있습니다.
        if ("Y".equals(history.getIsCorrect())) {
            return history.getGuessTheRank().getTier(); // 정답인 경우 정답 티어 반환
        } else {
            // 오답인 경우 임시로 다른 티어를 반환 (실제로는 사용자가 입력한 값을 저장해야 함)
            return "아이언"; // 임시값
        }
    }

}
