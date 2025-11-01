package com.back.app.service;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.back.app.repo.ItemTypeRepo;

import com.back.app.model.ItemType;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemTypeService {
    
    private final ItemTypeRepo itemTypeRepo;

    public List<ItemType> getAllItemTypes(){
        return itemTypeRepo.findAll();
    }
    public ItemType getItemTypebyId(Integer id){
        Optional<ItemType> optionalItemType = itemTypeRepo.findById(id);
        if(optionalItemType.isPresent()){
            return optionalItemType.get();
        }
        log.info("ItemType with id: {} doesn't exist", id);
        return null;
    }

   
}