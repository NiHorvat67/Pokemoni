package com.back.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.back.app.model.Account;
import com.back.app.model.AdverNoJoin;
import com.back.app.model.Advertisement;
import com.back.app.repo.AdverNoJoinRepo;
import com.back.app.repo.AdvertisementRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementService {

    private final AdvertisementRepo advertisementRepo;
    private final AdverNoJoinRepo adverNoJoinRepo;
    private final GeocodingService geocodingService;

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
        //Leonard
        try {
            String address = adverNoJoin.getAdvertisementLocationTakeover();
            Double[] latLon = geocodingService.searchLatLon(address);

            if (latLon != null) {
                adverNoJoin.setLatitude(latLon[0]);
                adverNoJoin.setLongitude(latLon[1]);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        //End
        return adverNoJoinRepo.save(adverNoJoin);
    }
    public Advertisement saveAdvertisement(Advertisement advertisement) {
        return advertisementRepo.save(advertisement);
    }

    public AdverNoJoin updateAdverNoJoin(Integer id, AdverNoJoin adverNoJoin) {
        adverNoJoin.setAdvertisementId(id);

        //Leonard
        //Upgrade: vidi da li je doslo do promjene u adresi samo onda radi request
        try {
            String address = adverNoJoin.getAdvertisementLocationTakeover();
            Double[] latLon = geocodingService.searchLatLon(address);
        
            if (latLon != null) {
                adverNoJoin.setLatitude(latLon[0]);
                adverNoJoin.setLongitude(latLon[1]);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        //End
        return adverNoJoinRepo.save(adverNoJoin);
    }

    public void deleteAccountById(Integer id) {
        advertisementRepo.deleteById(id);
    }

    public Advertisement getAdvertisementById(Integer id){
        return advertisementRepo.findByAdvertisementId(id);
    }

    public Advertisement getAdvertisementImageById(Integer id){
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