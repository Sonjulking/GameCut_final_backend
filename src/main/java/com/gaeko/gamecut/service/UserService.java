package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.mapper.UserMapper;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();

        return userMapper.toDTO(users);
    }

}
