package com.gaeko.gamecut.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.gaeko.gamecut.dto.ItemDTO;
import com.gaeko.gamecut.entity.Item;

@Mapper(componentModel = "spring", uses = {FileMapper.class})
public interface ItemMapper {

    // 단일 매핑
    ItemDTO toDTO(Item item);

    Item toEntity(ItemDTO dto);

    // 리스트 매핑
    List<ItemDTO> toDTOs(List<Item> items);
    
    
}
