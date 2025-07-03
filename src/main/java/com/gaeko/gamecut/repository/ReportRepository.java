package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {
	
	List<Report> findByUser_UserNo(Integer userNo);
}
