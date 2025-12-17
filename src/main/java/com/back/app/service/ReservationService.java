package com.back.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.back.app.model.Reservation;
import com.back.app.repo.ReservationRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepo ReservationRepo;

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

}