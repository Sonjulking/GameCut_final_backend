package com.gaeko.gamecut.service;

import com.gaeko.gamecut.entity.GuessTheRank;
import com.gaeko.gamecut.entity.GuessTheRankHistory;
import com.gaeko.gamecut.entity.Video;
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

    /** 1) 영상 업로드 뒤, tier 정보 저장 */
    @Transactional
    public GuessTheRank saveTier(Integer videoNo, String tier) {
        Video video = videoRepo.findById(videoNo)
            .orElseThrow(() -> new EntityNotFoundException("Video not found: " + videoNo));
        GuessTheRank gtr = GuessTheRank.builder()
            .video(video)
            .tier(tier)
            .build();
        return rankRepo.save(gtr);
    }

    /** 2) 무작위 퀴즈 한 건을 꺼내 옵니다. */
    @Transactional(readOnly = true)
    public GuessTheRank getRandomQuestion() {
        List<GuessTheRank> list = rankRepo.findRandomOne(PageRequest.of(0, 1));
        return list.stream()
                   .findFirst()
                   .orElseThrow(() -> new EntityNotFoundException("퀴즈 데이터가 없습니다."));
    }

    /** 3) 게임 중 답 제출 → 정답 여부 반환 & 히스토리 저장 */
    @Transactional
    public boolean submitAnswer(Integer gtrNo, String userAnswer) {
        GuessTheRank gtr = rankRepo.findById(gtrNo)
            .orElseThrow(() -> new EntityNotFoundException("GuessTheRank not found: " + gtrNo));
        boolean correct = gtr.getTier().equalsIgnoreCase(userAnswer);

        GuessTheRankHistory hist = GuessTheRankHistory.builder()
            .guessTheRank(gtr)
            .user(userService.getCurrentUser())
            .isCorrect(correct ? "Y" : "N")
            .solveDate(new Date())
            .build();
        historyRepo.save(hist);

        return correct;
    }

    @Transactional(readOnly=true)
    public List<GuessTheRank> getAll() {
        return rankRepo.findAll();
    }
}
