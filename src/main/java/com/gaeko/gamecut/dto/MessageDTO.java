package com.gaeko.gamecut.dto;

import java.util.Date;

import lombok.*;

@Getter
@Setter // ✅ 이 줄을 추가하세요
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private Integer messageNo;
    private Integer sendUserNo;
    private Integer receiveUserNo;
    private String messageContent;
    private Date messageDate;
    private Date messageDeleteDate;
    private String sendUserNickname; // 👈 추가
}
