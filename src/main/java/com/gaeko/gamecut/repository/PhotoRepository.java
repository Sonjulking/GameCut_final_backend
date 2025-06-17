package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.Photo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    @Transactional
    @Modifying
    void deletePhotoByBoard(Board board);
}
