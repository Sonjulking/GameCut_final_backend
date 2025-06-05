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
        uses = {PhotoMapper.class}
)
public interface UserMapper {

    @Mappings({
            @Mapping(target = "item", ignore = true),     // Item 매핑은 추후 ItemMapper 작성 시 추가 가능
            @Mapping(target = "photo", source = "photo")  // Photo → PhotoDTO (PhotoMapper)
    })
    UserDTO toDTO(User user);

    @Mappings({
            @Mapping(target = "item", ignore = true),
            @Mapping(target = "photo", ignore = true)     // DTO → 엔티티로 되돌릴 때는 photo 엔티티 처리는 별도 로직에서
    })
    User toEntity(UserDTO dto);

    List<UserDTO> toDTOs(List<User> users);
}
