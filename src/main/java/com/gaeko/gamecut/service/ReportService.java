package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.ReportDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.Report;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.repository.ReportRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public void saveReport(ReportDTO dto) {
        User user = userRepository.findById(dto.getUserNo())
                .orElseThrow(() -> new RuntimeException("신고자 없음"));
        Board board = boardRepository.findById(dto.getBoardNo())
                .orElseThrow(() -> new RuntimeException("신고 대상 게시글 없음"));

        Report report = Report.builder()
                .user(user)
                .board(board)
                .reportContent(dto.getReportContent())
                .reportType(dto.getReportType())
                .build();

        reportRepository.save(report);
    }

    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll().stream()
                .map(report -> ReportDTO.builder()
                        .reportNo(report.getReportNo())
                        .userNo(report.getUser().getUserNo())
                        .boardNo(report.getBoard().getBoardNo())
                        .reportContent(report.getReportContent())
                        .reportType(report.getReportType())
                        .reportDate(report.getReportDate())
                        .userNickname(report.getUser().getUserNickname())
                        .boardTitle(report.getBoard().getBoardTitle())
                        .userDeleteDate(report.getUser().getUserDeleteDate()) // 👈 추가
                        .build())
                .collect(Collectors.toList());
    }
}
