package com.back.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.back.app.repo.AdverNoJoinRepo;
import com.back.app.repo.AdvertisementRepo;
import com.back.app.model.Account;
import com.back.app.model.AdverNoJoin;
import com.back.app.model.Advertisement;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementService {

    private final AdvertisementRepo advertisementRepo;
    private final AdverNoJoinRepo adverNoJoinRepo;

    public List<Advertisement> getAllAdvertisements() {
        return advertisementRepo.findAll();
    }

    public List<Advertisement> getAllAdvertisementsByTrader(Integer id) {
        return advertisementRepo.findAllForTraderId(id);
    }



    public Advertisement hideSensitiveData(Advertisement ad) {

        if (ad.getTrader() != null) {
            Account safeTrader = new Account();
            safeTrader.setUserFirstName(ad.getTrader().getUserFirstName());
            safeTrader.setUserLastName(ad.getTrader().getUserLastName());
            safeTrader.setAccountRating(ad.getTrader().getAccountRating());
            safeTrader.setAccountId(ad.getTrader().getAccountId());
            safeTrader.setUserEmail(ad.getTrader().getUserEmail());
            safeTrader.setUserContact(ad.getTrader().getUserContact());
            safeTrader.setUserLocation(ad.getTrader().getUserLocation());

            ad.setTrader(safeTrader);
        }

        return ad;

    }

    public Advertisement getAdvertisementbyId(Integer id) {
        Optional<Advertisement> optionalAdvertisement = advertisementRepo.findById(id);
        if (optionalAdvertisement.isPresent()) {
            return optionalAdvertisement.get();
        }
        log.info("Advertisment with id: {} doesn't exist", id);
        return null;
    }

    public List<Advertisement> getFilteredAdvertisements(
            String itemName,
            Integer categoryId,
            LocalDate beginDate,
            LocalDate endDate,
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        log.info(
                "Searching advertisements with filters - categoryId: {}, beginDate: {}, endDate: {}, minPrice: {}, maxPrice: {}",
                categoryId, beginDate, endDate, minPrice, maxPrice);

        List<Advertisement> filteredAds = advertisementRepo.findFilteredAdvertisements(
                itemName, categoryId, beginDate, endDate, minPrice, maxPrice);

        log.info("Found {} advertisements matching the criteria", filteredAds.size());
        return filteredAds;
    }

    public AdverNoJoin saveAdverNoJoin(AdverNoJoin adverNoJoin) {
        return adverNoJoinRepo.save(adverNoJoin);
    }

    public AdverNoJoin updateAdverNoJoin(Integer id, AdverNoJoin adverNoJoin) {
        adverNoJoin.setAdvertisementId(id);
        return adverNoJoinRepo.save(adverNoJoin);
    }

    public void deleteAccountById(Integer id) {
        advertisementRepo.deleteById(id);
    }

    public Advertisement getAdvertisementById(Integer id){
        return advertisementRepo.findByAdvertisementId(id);
    }


    public List<Advertisement> getAdvertisementsByIds(List<Integer> ids) {
        log.info("Fetching advertisements for IDs: {}", ids);
        
        List<Advertisement> ads = advertisementRepo.findAllByAdvertisementIdIn(ids);
        
        return ads.stream()
                .map(this::hideSensitiveData)
                .collect(Collectors.toList());
    }

}