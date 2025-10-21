package com.back.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.back.app.model.Advertisement;
import com.back.app.service.AdvertisementService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/advertisements/v1")
@RequiredArgsConstructor
@Validated
public class AdvertisementController {
    
    private final AdvertisementService advertisementService;

    // permited for all
    @GetMapping("/")
    public ResponseEntity<List<Advertisement>> getAllAdvertisements(){
        return ResponseEntity.ok().body(advertisementService.getAllAdvertisements());
    }

    // permited for all
    @GetMapping("/{id}")
    public ResponseEntity<Advertisement> getMethodName(@PathVariable Integer id) {
        return ResponseEntity.ok().body(advertisementService.getAdvertisementbyId(id));
    }
    

}
