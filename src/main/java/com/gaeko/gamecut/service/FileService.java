package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.mapper.FileMapper;
import com.gaeko.gamecut.mapper.PhotoMapper;
import com.gaeko.gamecut.mapper.UserMapper;
import com.gaeko.gamecut.repository.FileRepository;
import com.gaeko.gamecut.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final FileMapper fileMapper;

    //파일저장
    @Transactional
    public FileDTO save(FileDTO dto) {
        File file = fileMapper.toEntity(dto);

        if (dto.getUserNo() != null) {
            UserDTO userDto = userService.findUserByUserNo(dto.getUserNo());
            User user = userMapper.toEntity(userDto);
            file.setUser(user);
        }


        File saved = fileRepository.save(file);
        return fileMapper.toDTO(saved);
    }

}
