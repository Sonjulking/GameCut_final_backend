package com.gaeko.gamecut.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {

    @Id
    @Column(name = "test_id")
    private int testId;

    @Column(name = "test_title")
    private String testTitle;

    @Column(name = "test_name")
    private String testName;
}
