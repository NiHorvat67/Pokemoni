package com.back.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.back.app.model.Advertisement;


public interface AdvertisementRepo extends JpaRepository<Advertisement ,Integer>{
    
}
