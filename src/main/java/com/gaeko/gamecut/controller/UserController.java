package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    public List<UserDTO> findAll() {
        return userService.findAll();
    }
}
