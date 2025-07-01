package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.entity.Item;
import com.gaeko.gamecut.entity.User;
import org.mapstruct.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * User 엔티티 ↔ UserDTO 매핑
 * - item 필드는 아직 매핑 로직이 없으므로 무시(ignore)
 * - photo 필드는 PhotoMapper를 통해 PhotoDTO로 매핑
 */
@Mapper(
	    componentModel = "spring",
	    uses = { PhotoMapper.class, ItemMapper.class }
	)
	public interface UserMapper {

	    @Mapping(target = "item", ignore = true)
	    @Mapping(target = "photo", ignore = true)
	    User toEntity(UserDTO dto);

	    UserDTO toDTO(User user);

	    List<UserDTO> toDTOs(List<User> users);
	    
	    List<User> toEntities(List<UserDTO> dtos);
	    
	}



