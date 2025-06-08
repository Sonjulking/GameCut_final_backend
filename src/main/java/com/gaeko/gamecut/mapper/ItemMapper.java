package com.gaeko.gamecut.mapper;

import com.gaeko.gamecut.dto.ItemDTO;
import com.gaeko.gamecut.entity.Item;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FileMapper.class})
public interface ItemMapper {

    // 단일 매핑
    ItemDTO toDTO(Item item);

    Item toEntity(ItemDTO dto);

    // 리스트 매핑
    List<ItemDTO> toDTOs(List<Item> items);
}
