package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Message;
import com.gaeko.gamecut.entity.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    // 필요시 수신자/발신자 기준 조회 메서드 추가 가능
	List<Message> findByReceiveUser(User receiveUser);
	List<Message> findBySendUser(User sendUser);


}
