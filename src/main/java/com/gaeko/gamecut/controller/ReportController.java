package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.ReportDTO;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.service.ReportService;
import com.gaeko.gamecut.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;

    @PostMapping
    public Map<String, Object> report(@RequestBody ReportDTO dto) {
        try {
            User currentUser = userService.getCurrentUser();
            dto.setUserNo(currentUser.getUserNo()); // 🔐 보안
            reportService.saveReport(dto);
            return Map.of("success", true, "message", "신고가 접수되었습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }
    
    @GetMapping("/admin/list")
    public List<ReportDTO> getAllReports() {
        User currentUser = userService.getCurrentUser();
        if (!"ROLE_ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }
        return reportService.getAllReports();
    }


}
