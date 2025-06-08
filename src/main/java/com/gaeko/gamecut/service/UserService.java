package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.entity.Photo;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.mapper.UserMapper;
import com.gaeko.gamecut.repository.PhotoRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    public UserDTO findUserByUserNo(Integer userNo) {
        User user = userRepository.findUserByUserNo(userNo);
        return userMapper.toDTO(user);
    }

    public List<UserDTO> findAll() {
        // DB에서 모든 User 엔티티를 가져온 뒤,
        // UserMapper의 toDTOs(List<User>)를 호출해야 합니다.
        List<User> users = userRepository.findAll();
        return userMapper.toDTOs(users);
    }

    //DB에 유저정보 저장
    public void saveUser(UserDTO userDTO, Integer photoNo) {
        User user = userMapper.toEntity(userDTO);

        if (photoNo != null) {
            Photo photo = photoRepository.findById(photoNo).orElse(null);
            user.setPhoto(photo);
        }

        userRepository.save(user);
    }

}
