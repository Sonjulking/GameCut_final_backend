package com.gaeko.gamecut.dto;

import java.util.Date;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {
    private Integer boardNo;           
    private Integer userNo;           
    private String boardTypeNo;      // ❌ Integer → ✅ String (Entity와 일치)
    private Integer videoNo;     
    private String boardContent;  
    private String boardTitle;     
    private Integer boardCount;       
    private Integer boardLike;         
    private Date boardCreateDate;  
    private Date boardDeleteDate; 
    private boolean likedByCurrentUser;
    
    // 추가 권장 필드들 (화면 표시용)
    private String userNickname;        // 작성자 닉네임
    private String boardTypeName;       // 게시판 타입명
    private String profileImageUrl;     // 작성자 프로필 이미지
}