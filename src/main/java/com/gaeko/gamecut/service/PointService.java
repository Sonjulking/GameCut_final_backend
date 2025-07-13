package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.PointHistoryDTO;
import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.entity.PointHistory;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.PointHistoryRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 포인트 사용 내역 저장
     * @param userDTO 사용자 정보
     * @param point 포인트 양 (양수: 획득, 음수: 사용)
     * @param reason 획득/사용처
     */
    @Transactional
    public void insertHistory(UserDTO userDTO, Integer point, String reason) {
        // UserDTO에서 User 엔티티 조회
        User user = userRepository.findByUserId(userDTO.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: " + userDTO.getUserId()));
        
        // PointHistory 엔티티 생성
        PointHistory pointHistory = PointHistory.builder()
            .user(user)
            .pointAmount(point)  // 양수면 획득, 음수면 사용
            .pointSource(reason)
            .build();
        
        // 저장
        pointHistoryRepository.save(pointHistory);
        
        // 로그 출력
        String action = point > 0 ? "획득" : "사용";
        System.out.println(String.format(
            "포인트 이력 저장 완료 - 사용자: %s, %s: %d점, 사유: %s", 
            userDTO.getUserId(), action, Math.abs(point), reason
        ));
    }
}
