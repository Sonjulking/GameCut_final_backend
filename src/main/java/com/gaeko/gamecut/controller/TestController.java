package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.TestDTO;
import com.gaeko.gamecut.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("list", testService.findAll());
        return "test/test";
    }

}
