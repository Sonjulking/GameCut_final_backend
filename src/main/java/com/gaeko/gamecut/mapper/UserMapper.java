package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);

    User toEntity(UserDTO dto);

    List<UserDTO> toDTO(List<User> userList);

    List<User> toEntity(List<UserDTO> dtoList);
}
