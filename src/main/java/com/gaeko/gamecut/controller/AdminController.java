package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/user/delete/{userNo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Integer userNo) {
        try {
            adminService.softDeleteUser(userNo);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "유저 탈퇴 처리 완료"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "탈퇴 처리 실패: " + e.getMessage()
            ));
        }
    }
}
