package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.dto.BoardTypeDTO;
import com.gaeko.gamecut.entity.BoardType;
import com.gaeko.gamecut.mapper.BoardTypeMapper;
import com.gaeko.gamecut.repository.BoardTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardTypeService {
    private final BoardTypeRepository boardTypeRepository;
    private final BoardTypeMapper boardTypeMapper;

    public BoardTypeDTO findByBoardTypeNo(int boardTypeNo) {
        BoardType boardType = boardTypeRepository.findBoardTypeByBoardTypeNo(boardTypeNo);
        BoardTypeDTO boardTypeDTO = boardTypeMapper.toDTO(boardType);
        return boardTypeDTO;
    }
}
