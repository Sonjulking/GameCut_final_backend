package com.gaeko.gamecut.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gaeko.gamecut.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
	
	Optional<Item> findById(Integer itemNo);

}
