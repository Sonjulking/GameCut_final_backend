package com.gaeko.gamecut.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gaeko.gamecut.dto.ItemDTO;
import com.gaeko.gamecut.service.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    
    // 아이템 조회
    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        List<ItemDTO> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    // 아이템(이미지) 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> uploadItem(
            @RequestPart("item") ItemDTO itemDTO,
            @RequestPart("file") MultipartFile file,
            Principal principal
    ) {
        try {
            ItemDTO savedItem = itemService.uploadItem(itemDTO, file, principal.getName());
            return ResponseEntity.ok(savedItem);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }
    }
    
    @PostMapping("/buy")
    public ResponseEntity<?> buyItem(
            @RequestParam Integer itemNo,
            Principal principal
    ) {
        try {
            ItemDTO purchasedItem = itemService.buyItem(itemNo, principal.getName());
            return ResponseEntity.ok(purchasedItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    @GetMapping("/my")
    public ResponseEntity<List<ItemDTO>> getMyItems(Principal principal) {
        List<ItemDTO> myItems = itemService.getMyItems(principal.getName());
        return ResponseEntity.ok(myItems);
    }

}