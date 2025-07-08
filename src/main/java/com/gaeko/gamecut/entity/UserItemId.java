package com.gaeko.gamecut.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserItemId implements Serializable {

    private Integer user; // User 엔티티의 userNo
    private Integer item; // Item 엔티티의 itemNo
}
