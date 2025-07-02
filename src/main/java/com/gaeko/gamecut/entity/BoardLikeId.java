package com.gaeko.gamecut.entity;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BoardLikeId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer user;
    private Integer board;
}