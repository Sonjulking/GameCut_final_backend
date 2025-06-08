package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.entity.User;
import org.mapstruct.*;

import java.util.List;

/**
 * User 엔티티 ↔ UserDTO 매핑
 * - item 필드는 아직 매핑 로직이 없으므로 무시(ignore)
 * - photo 필드는 PhotoMapper를 통해 PhotoDTO로 매핑
 */
@Mapper(
        componentModel = "spring",
        uses = {
                PhotoMapper.class,
                ItemMapper.class
        }
        //ItemMapper.class도  필요함
)
//자바에서는 인터페이스는 인스턴스가 불가능함.
public interface UserMapper {
    UserDTO toDTO(User user);

    User toEntity(UserDTO dto);

    List<UserDTO> toDTOs(List<User> users);
}
