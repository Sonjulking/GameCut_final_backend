package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.PointHistoryDTO;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.PointHistoryRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointHistoryRepository pointHistoryRepository;
    private final UserRepository userRepository;

    public List<Map<String, Object>> getMonthlyRanking() {
        List<Object[]> results = pointHistoryRepository.findMonthlyRanking();

        return results.stream().map(obj -> {
        	Number userNoNum = (Number) obj[0];
        	Number totalPointsNum = (Number) obj[1];
        	Integer userNo = userNoNum.intValue();
        	Integer totalPoints = totalPointsNum.intValue();

            User user = userRepository.findUserByUserNo(userNo);

            Map<String, Object> map = new HashMap<>();
            map.put("userNo", userNo);
            map.put("nickname", user.getUserNickname());
            map.put("totalPoints", totalPoints);
            return map;
        }).collect(Collectors.toList());
    }
    
    
    // 누적 랭킹
    public List<Map<String, Object>> getTotalRanking() {
        List<Object[]> results = pointHistoryRepository.findTotalRanking();

        return results.stream().map(obj -> {
        	Number userNoNum = (Number) obj[0];
        	Number totalPointsNum = (Number) obj[1];
        	Integer userNo = userNoNum.intValue();
        	Integer totalPoints = totalPointsNum.intValue();

            User user = userRepository.findUserByUserNo(userNo);

            Map<String, Object> map = new HashMap<>();
            map.put("userNo", userNo);
            map.put("nickname", user.getUserNickname());
            map.put("totalPoints", totalPoints);
            return map;
        }).collect(Collectors.toList());
    }
    
    
    public List<PointHistoryDTO> getPointHistoryByUserId(String userId) {
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: " + userId));

        return pointHistoryRepository.findByUser(user).stream()
                .map(p -> PointHistoryDTO.builder()
                        .pointHistoryNo(p.getPointHistoryNo())
                        .userNo(user.getUserNo())
                        .pointAmount(p.getPointAmount())
                        .pointDate(p.getPointDate())
                        .pointSource(p.getPointSource())
                        .build())
                .collect(Collectors.toList());
    }


}
