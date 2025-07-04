package com.gaeko.gamecut.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.dto.ItemDTO;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.entity.Item;
import com.gaeko.gamecut.entity.PointHistory;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.entity.UserItem;
import com.gaeko.gamecut.repository.FileRepository;
import com.gaeko.gamecut.repository.ItemRepository;
import com.gaeko.gamecut.repository.PointHistoryRepository;
import com.gaeko.gamecut.repository.UserItemRepository;
import com.gaeko.gamecut.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final FileRepository fileRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserItemRepository userItemRepository; // ✅ 올바르게 주입

    // 아이템 전체 조회
    private ItemDTO convertToDTO(Item item) {
        File file = item.getItemImage();

        FileDTO fileDTO = FileDTO.builder()
                .attachNo(file.getAttachNo())
                .fileUrl(file.getFileUrl())
                .realPath(file.getRealPath())
                .mimeType(file.getMimeType())
                .uploadTime(file.getUploadTime())
                .originalFileName(file.getOriginalFileName())
                .uuid(file.getUuid())
                .userNo(file.getUser().getUserNo())
                .build();

        return ItemDTO.builder()
                .itemNo(item.getItemNo())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .itemDeleteDate(item.getItemDeleteDate())
                .itemImage(fileDTO)
                .build();
    }

    public List<ItemDTO> getAllItems() {
        List<Item> itemList = itemRepository.findAll();
        return itemList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 아이템 업로드
    public ItemDTO uploadItem(ItemDTO itemDTO, MultipartFile file, String username) throws IOException {
        FileDTO savedFileDTO = fileUploadService.store(file);

        User uploader = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("업로더 없음"));

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

        Item item = new Item();
        item.setItemName(itemDTO.getItemName());
        item.setItemPrice(itemDTO.getItemPrice());
        item.setItemDeleteDate(itemDTO.getItemDeleteDate());
        item.setItemImage(fileEntity);

        itemRepository.save(item);

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
        
        // 아이템 중복 구매 방지 
        boolean alreadyOwned = userItemRepository.existsByUserAndItem(user, item);
        if (alreadyOwned) {
            throw new IllegalArgumentException("이미 구매한 아이템입니다.");
        }
        
        if (user.getUserPoint() < item.getItemPrice()) {
            throw new IllegalArgumentException("포인트 부족");
        }

        // 포인트 차감 및 저장
        user.setUserPoint(user.getUserPoint() - item.getItemPrice());
        userRepository.save(user);

        // 유저가 구매한 아이템 저장
        UserItem userItem = UserItem.builder()
                .user(user)
                .item(item)
                .build();
        userItemRepository.save(userItem);

        // 포인트 사용 기록 추가
        PointHistory pointHistory = PointHistory.builder()
                .user(user)
                .pointAmount(-item.getItemPrice()) // 마이너스 값
                .pointSource("아이템 구매: " + item.getItemName())
                .build();
        pointHistoryRepository.save(pointHistory);

        // DTO 반환
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
    
    public List<ItemDTO> getMyItems(String username) {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        List<UserItem> userItems = userItemRepository.findByUser(user);

        return userItems.stream()
                .map(userItem -> convertToDTO(userItem.getItem()))
                .collect(Collectors.toList());
    }

}
