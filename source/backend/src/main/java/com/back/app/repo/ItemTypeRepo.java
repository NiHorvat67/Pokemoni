package com.back.app.repo;



import org.springframework.data.jpa.repository.JpaRepository;

import com.back.app.model.ItemType;

public interface ItemTypeRepo extends JpaRepository<ItemType, Integer>{

    
}
