package com.gaeko.gamecut.entity;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommentLikeId implements Serializable {
    private Integer comment;
    private Integer user;
    
    private static final long serialVersionUID = 1L;
}