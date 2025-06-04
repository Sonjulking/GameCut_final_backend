package com.gaeko.gamecut.dto;

import java.util.Date;

import lombok.*;

@Getter
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
}
