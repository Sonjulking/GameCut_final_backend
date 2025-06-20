package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository  extends JpaRepository<Photo, Integer> {
}
