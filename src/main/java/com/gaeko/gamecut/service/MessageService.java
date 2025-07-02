package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.MessageDTO;
import com.gaeko.gamecut.entity.Message;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.MessageRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendMessage(MessageDTO dto) {
        User sender = userRepository.findById(dto.getSendUserNo())
                .orElseThrow(() -> new RuntimeException("발신자 없음"));
        User receiver = userRepository.findById(dto.getReceiveUserNo())
                .orElseThrow(() -> new RuntimeException("수신자 없음"));

        Message message = Message.builder()
                .sendUser(sender)
                .receiveUser(receiver)
                .messageContent(dto.getMessageContent())
                .build();

        messageRepository.save(message);
    }
    
    
    // MessageService.java
    public List<MessageDTO> getReceivedMessages(User user) {
        return messageRepository.findByReceiveUser(user).stream()
                .map(this::convertToDto)
                .toList();
    }

    public void softDeleteMessage(Integer messageNo) {
        Message message = messageRepository.findById(messageNo)
            .orElseThrow(() -> new RuntimeException("쪽지 없음"));
        message.setMessageDeleteDate(new Date());
        messageRepository.save(message);
    }

    // DTO 변환 메서드도 Service 안에 추가
    private MessageDTO convertToDto(Message message) {
        return MessageDTO.builder()
                .messageNo(message.getMessageNo())
                .sendUserNo(message.getSendUser().getUserNo())
                .receiveUserNo(message.getReceiveUser().getUserNo())
                .messageContent(message.getMessageContent())
                .sendUserNickname(message.getSendUser().getUserNickname()) // 👈 추가
                .messageDate(message.getMessageDate())
                .messageDeleteDate(message.getMessageDeleteDate())
                .build();
    }
    
    public List<MessageDTO> getSentMessages(User user) {
        return messageRepository.findBySendUser(user).stream()
                .map(this::convertToDto)
                .toList();
    }


}
