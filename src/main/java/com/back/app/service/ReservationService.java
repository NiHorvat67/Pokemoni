package com.back.app.service;

import java.time.LocalDateTime;
import java.util.Comparator;
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

    private final ReservationRepo reservationRepo;
    private final AdvertisementService advertisementService;

    public List<Reservation> getAllReservations() {
        return reservationRepo.findAll();
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepo.save(reservation);
    }

    public Reservation getReservationbyId(Integer id) {
        Optional<Reservation> optionalReservation = reservationRepo.findById(id);
        if (optionalReservation.isPresent()) {
            return optionalReservation.get();
        }
        log.info("Reservation with id: {} doesn't exist", id);
        return null;
    }

    public Reservation updateReservation(Integer id, Reservation reservation) {
        reservation.setReservationId(id);
        return reservationRepo.save(reservation);
    }

    public List<Reservation> getReservationsByBuyerId(Integer buyerId) {
        return reservationRepo.findByBuyerId(buyerId);
    }

    public List<Reservation> getResByAdvertisementId(Integer advertisementId) {
        return reservationRepo.findByAdvertisementId(advertisementId);
    }

    public List<DateInterval> getResvDateIntervalsForAdId(Integer advertisementId) {
        return getResByAdvertisementId(advertisementId).stream()
                .map(r -> new DateInterval(r.getReservationStart(), r.getReservationEnd()))
                .toList();
    }

    public boolean hasNoHoles(Integer advertisementId) {
        Advertisement advertisement = advertisementService.getAdvertisementbyId(advertisementId);
        if (advertisement == null) {
            return true; 
        }

        DateInterval adBounds = new DateInterval(
                advertisement.getAdvertisementStart(),
                advertisement.getAdvertisementEnd());

        List<DateInterval> existingIntervals = getResvDateIntervalsForAdId(advertisementId);


        if (existingIntervals.isEmpty()) {
            return false;
        }
        List<DateInterval> sortedIntervals = existingIntervals.stream()
                .sorted(Comparator.comparing(DateInterval::getStartDate))
                .toList();

        if (adBounds.getStartDate().isBefore(sortedIntervals.get(0).getStartDate())) {
            return false;
        }

        for (int i = 0; i < sortedIntervals.size() - 1; i++) {
            DateInterval current = sortedIntervals.get(i);
            DateInterval next = sortedIntervals.get(i + 1);
            if (current.getEndDate().plusDays(1).isBefore(next.getStartDate())) {
                return false;
            }
        }

        DateInterval lastReservation = sortedIntervals.get(sortedIntervals.size() - 1);
        if (lastReservation.getEndDate().isBefore(adBounds.getEndDate())) {
            return false;
        }
        return true; 
    }


    public boolean isReservationIntervalFreeForAd(DateInterval dateInterval, Integer advertisementId) {
        List<DateInterval> existingIntervals = getResvDateIntervalsForAdId(advertisementId);

        Advertisement advertisement = advertisementService.getAdvertisementbyId(advertisementId);
        if (advertisement == null) {
            return false;
        }

        DateInterval advertisementBounds = new DateInterval(
                advertisement.getAdvertisementStart(),
                advertisement.getAdvertisementEnd());

        return isIntervalAvailable(dateInterval, existingIntervals, advertisementBounds);
    }



    public boolean isIntervalAvailable(DateInterval newInterval,
            List<DateInterval> existingIntervals,
            DateInterval advertisementBounds) {

        if (newInterval.getStartDate().isBefore(advertisementBounds.getStartDate()) ||
                newInterval.getEndDate().isAfter(advertisementBounds.getEndDate())) {
            return false;
        }

        for (DateInterval existing : existingIntervals) {
            if (newInterval.overlapsWith(existing)) {
                return false;
            }
        }

        return true;
    }

    public Integer getTraderIdForReservationId(Integer reservationId) {
        Optional<Reservation> reservationOpt = reservationRepo.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            log.info("Reservation with id {} not found", reservationId);
            return null;
        }

        Reservation reservation = reservationOpt.get();

        Advertisement advertisement = advertisementService.getAdvertisementbyId(reservation.getAdvertisementId());
        if (advertisement == null) {
            log.info("Advertisement not found for reservation {}", reservationId);
            return null;
        }

        return advertisement.getTrader().getAccountId();
    }

    public List<Reservation> getReservationsByTraderId(Integer traderId) {
        List<Advertisement> advertisements = advertisementService.getAllAdvertisementsByTrader(traderId);
        List<Integer> advertisementIds = advertisements.stream()
                .map(Advertisement::getAdvertisementId)
                .collect(Collectors.toList());

        if (advertisementIds.isEmpty()) {
            return List.of();
        }

        return reservationRepo.findByAdvertisementIdIn(advertisementIds);
    }

    public String determineStatus(Reservation reservation) {
        LocalDateTime reservationRequestEnded = reservation.getReservationRequestEnded();
        if (reservationRequestEnded == null) {
            return "ACTIVE";
        }

        LocalDateTime now = LocalDateTime.now();
        if (reservationRequestEnded.isBefore(now)) {
            return "NOT ACTIVE";
        } else {
            return "ACTIVE";
        }
    }

}