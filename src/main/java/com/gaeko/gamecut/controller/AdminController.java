package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/user/delete/{userNo}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userNo) {
        try {
            adminService.softDeleteUser(userNo);
            return ResponseEntity.ok().body("유저 탈퇴 처리 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("탈퇴 처리 실패: " + e.getMessage());
        }
    }
}
