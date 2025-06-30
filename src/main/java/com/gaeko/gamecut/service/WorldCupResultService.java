// src/main/java/com/gaeko/gamecut/service/WorldCupResultService.java
package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.MadmovieWorldCupResultDTO;
import com.gaeko.gamecut.dto.SaveWorldCupResultRequest;
import com.gaeko.gamecut.entity.MadmovieWorldCupResult;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.entity.Video;
import com.gaeko.gamecut.repository.MadmovieWorldCupResultRepository;
import com.gaeko.gamecut.repository.VideoRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorldCupResultService {
  private final MadmovieWorldCupResultRepository repo;
  private final VideoRepository videoRepo;
  private final UserRepository userRepo;

 public MadmovieWorldCupResultDTO saveChampion(Integer userNo, Integer videoNo) {
    if (videoNo == null) {
        throw new IllegalArgumentException("videoNo must not be null");
    }
    Video video = videoRepo.findById(videoNo)
        .orElseThrow(() -> new IllegalArgumentException("Invalid videoNo: " + videoNo));
    User user = userRepo.findById(userNo)
        .orElseThrow(() -> new IllegalArgumentException("Invalid userNo: " + userNo));

    MadmovieWorldCupResult saved = repo.save(
        MadmovieWorldCupResult.builder()
            .video(video)
            .user(user)
            .build()
    );
    return MadmovieWorldCupResultDTO.builder()
        .worldCupNo(saved.getWorldCupNo())
        .videoNo(videoNo)
        .userNo(userNo)
        .winDate(saved.getWinDate())
        .build();
}

}
