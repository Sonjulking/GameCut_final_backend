package com.gaeko.gamecut.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Integer reportNo;       
    private Integer userNo;         
    private Integer boardNo;        
    private String reportContent; 
    private String reportType;    
    private Date reportDate;
    
    private String userNickname; // 🔹신고자 닉네임
    private String boardTitle;   // 🔹게시글 제목
    
    private LocalDateTime userDeleteDate; // 👈 추가

}
