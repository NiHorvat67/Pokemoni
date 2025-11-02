package com.back.app.repo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.back.app.model.ItemType;

public interface ItemTypeRepo extends JpaRepository<ItemType, Integer>{

    
}
