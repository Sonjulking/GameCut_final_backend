package com.gaeko.gamecut.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.dto.ItemDTO;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.entity.Item;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.FileRepository;
import com.gaeko.gamecut.repository.ItemRepository;
import com.gaeko.gamecut.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final FileRepository fileRepository;

    // 아이템 업로드 
    public ItemDTO uploadItem(ItemDTO itemDTO, MultipartFile file, String username) throws IOException {
        // 1. 파일 업로드 (FileDTO 반환)
        FileDTO savedFileDTO = fileUploadService.store(file);

        // 2. 업로더 정보로 User 엔티티 조회
        User uploader = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("업로더 없음"));

        // 3. File 엔티티 생성 및 저장
        File fileEntity = File.builder()
                .user(uploader)
                .uuid(savedFileDTO.getUuid())
                .fileUrl(savedFileDTO.getFileUrl())
                .realPath(savedFileDTO.getRealPath())
                .mimeType(savedFileDTO.getMimeType())
                .uploadTime(savedFileDTO.getUploadTime())
                .originalFileName(savedFileDTO.getOriginalFileName())
                .build();
        fileRepository.save(fileEntity);

        // 4. Item 생성 및 저장
        Item item = new Item();
        item.setItemName(itemDTO.getItemName());
        item.setItemPrice(itemDTO.getItemPrice());
        item.setItemDeleteDate(itemDTO.getItemDeleteDate());
        item.setItemImage(fileEntity);

        itemRepository.save(item);

        // 5. ItemDTO로 반환
        return ItemDTO.builder()
                .itemNo(item.getItemNo())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .itemDeleteDate(item.getItemDeleteDate())
                .itemImage(savedFileDTO)
                .build();
    }
    
    // 아이템 구매 
    public ItemDTO buyItem(Integer itemNo, String username) {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        Item item = itemRepository.findById(itemNo)
                .orElseThrow(() -> new IllegalArgumentException("아이템 없음"));

        if (user.getUserPoint() < item.getItemPrice()) {
            throw new IllegalArgumentException("포인트 부족");
        }
        user.setUserPoint(user.getUserPoint() - item.getItemPrice());
        userRepository.save(user);

        // File → FileDTO 변환
        File fileEntity = item.getItemImage();
        FileDTO fileDTO = FileDTO.builder()
                .attachNo(fileEntity.getAttachNo())
                .fileUrl(fileEntity.getFileUrl())
                .realPath(fileEntity.getRealPath())
                .mimeType(fileEntity.getMimeType())
                .uploadTime(fileEntity.getUploadTime())
                .originalFileName(fileEntity.getOriginalFileName())
                .uuid(fileEntity.getUuid())
                .userNo(fileEntity.getUser() != null ? fileEntity.getUser().getUserNo() : null)
                .build();

        return ItemDTO.builder()
                .itemNo(item.getItemNo())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .itemDeleteDate(item.getItemDeleteDate())
                .itemImage(fileDTO)
                .build();
    }

}

