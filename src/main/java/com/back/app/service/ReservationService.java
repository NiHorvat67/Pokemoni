package com.back.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.back.app.model.Advertisement;
import com.back.app.model.Reservation;
import com.back.app.repo.ReservationRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepo ReservationRepo;
    private final AdvertisementService AdvertisementService;

    public List<Reservation> getAllReservations() {
        return ReservationRepo.findAll();
    }

    public Reservation saveReservation(Reservation reservation) {
        return ReservationRepo.save(reservation);
    }

    public Reservation getReservationbyId(Integer id) {
        Optional<Reservation> optionalReservation = ReservationRepo.findById(id);
        if (optionalReservation.isPresent()) {
            return optionalReservation.get();
        }
        log.info("Reservation with id: {} doesn't exist", id);
        return null;
    }



    public Reservation updateReservation(Integer id, Reservation reservation) {
        reservation.setReservationId(id);
        return ReservationRepo.save(reservation);
    }

    public List<Reservation> getReservationsByBuyerId(Integer buyerId) {
        return ReservationRepo.findByBuyerId(buyerId);
    }

    public List<Reservation> getReservationsByTraderId(Integer traderId) {
        List<Advertisement> advertisements = AdvertisementService.getAllAdvertisementsByTrader(traderId);
        List<Integer> advertisementIds = advertisements.stream()
                .map(Advertisement::getAdvertisementId)
                .collect(Collectors.toList());
        
        if (advertisementIds.isEmpty()) {
            return List.of();
        }
        
        return ReservationRepo.findByAdvertisementIdIn(advertisementIds);
    }

    public String determineStatus(Reservation reservation) {
        LocalDateTime reservationEnd = reservation.getReservationEnd();
        if (reservationEnd == null) {
            return "ACTIVE";
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (reservationEnd.isBefore(now)) {
            return "NOT ACTIVE";
        } else {
            return "ACTIVE";
        }
    }

}