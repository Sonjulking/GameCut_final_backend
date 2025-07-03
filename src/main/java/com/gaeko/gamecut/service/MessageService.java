package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.MessageDTO;
import com.gaeko.gamecut.entity.Block;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.entity.Message;
import com.gaeko.gamecut.repository.BlockRepository;
import com.gaeko.gamecut.repository.MessageRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    @Transactional
    public void sendMessage(MessageDTO dto) {
        User sender = userRepository.findById(dto.getSendUserNo())
                .orElseThrow(() -> new RuntimeException("발신자 없음"));
        User receiver = userRepository.findById(dto.getReceiveUserNo())
                .orElseThrow(() -> new RuntimeException("수신자 없음"));

        // ❗ 차단 여부 확인: 수신자가 발신자를 차단한 경우 예외
        boolean isBlocked = blockRepository.existsByBlockerAndBlocked(receiver, sender);
        if (isBlocked) {
            throw new IllegalStateException("쪽지를 보낼 수 없습니다. 수신자가 당신을 차단했습니다.");
        }

        Message message = Message.builder()
                .sendUser(sender)
                .receiveUser(receiver)
                .messageContent(dto.getMessageContent())
                .build();

        messageRepository.save(message);
    }

    public List<MessageDTO> getReceivedMessages(User user) {
        return messageRepository.findByReceiveUser(user).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<MessageDTO> getSentMessages(User user) {
        return messageRepository.findBySendUser(user).stream()
                .map(this::convertToDto)
                .toList();
    }

    public void softDeleteMessage(Integer messageNo) {
        Message message = messageRepository.findById(messageNo)
                .orElseThrow(() -> new RuntimeException("쪽지 없음"));
        message.setMessageDeleteDate(new Date());
        messageRepository.save(message);
    }

    private MessageDTO convertToDto(Message message) {
        return MessageDTO.builder()
                .messageNo(message.getMessageNo())
                .sendUserNo(message.getSendUser().getUserNo())
                .receiveUserNo(message.getReceiveUser().getUserNo())
                .messageContent(message.getMessageContent())
                .sendUserNickname(message.getSendUser().getUserNickname()) // 닉네임 포함
                .messageDate(message.getMessageDate())
                .messageDeleteDate(message.getMessageDeleteDate())
                .build();
    }
}
