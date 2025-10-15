package com.back.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.back.app.repo.AdvertisementRepo;
import com.back.app.model.Advertisement;



@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementService {
    
    private final AdvertisementRepo advertisementRepo;

    public List<Advertisement> getAllAdvertisements(){
        return advertisementRepo.findAll();
    }

    public Advertisement getAdvertisementbyId(Integer id){

        Optional<Advertisement> optionalAdvertisement = advertisementRepo.findById(id);
        if(optionalAdvertisement.isPresent()){
            return optionalAdvertisement.get();
        }
        log.info("Employee with id: {} doesn't exist", id);
        return null;
    }

}
