package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.BoardDTO;
import com.gaeko.gamecut.dto.PhotoDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.entity.Photo;
import com.gaeko.gamecut.mapper.BoardMapper;
import com.gaeko.gamecut.mapper.PhotoMapper;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.repository.FileRepository;
import com.gaeko.gamecut.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final BoardMapper boardMapper;
    private final FileRepository fileRepository;
    private final BoardRepository boardRepository;

    public PhotoDTO save(Integer boardNo, Integer attachNo, Integer photoOrder) {
        Photo photo = new Photo();
        File file = fileRepository.findFileByAttachNo(attachNo);
        Board board = boardRepository.findBoardByBoardNo(boardNo);

        photo.setAttachFile(file);
        photo.setBoard(board);
        photo.setPhotoOrder(photoOrder);

        photo = photoRepository.save(photo);
        return photoMapper.toDTO(photo);

    }

    // PhotoService
    public void deleteByBoardNo(BoardDTO boardDTO) {
        Board board = boardMapper.toEntity(boardDTO);
        photoRepository.deletePhotoByBoard(board);
    }
}
