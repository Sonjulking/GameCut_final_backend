// src/main/java/com/gaeko/gamecut/entity/BlockId.java
package com.gaeko.gamecut.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BlockId implements Serializable {
    private Integer blocker;
    private Integer blocked;
}
