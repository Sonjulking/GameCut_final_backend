package com.gaeko.gamecut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gaeko.gamecut.entity.GuessTheRankHistory;

@Repository
public interface GuessTheRankHistoryRepository extends JpaRepository<GuessTheRankHistory, Integer> {}