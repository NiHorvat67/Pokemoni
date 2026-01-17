package com.back.app.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.back.app.model.Reservation;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByBuyerId(Integer buyerId);

    List<Reservation> findByAdvertisementId(Integer AdvertisementId);

    List<Reservation> findByAdvertisementIdIn(List<Integer> advertisementIds);

    @Query("SELECT r FROM Reservation r WHERE " +
            "CAST(r.reservationEnd AS LocalDateTime) <= :currentDateTime " +
            "AND r.reservationRequestEnded IS NULL")
    List<Reservation> findEndedReservationsWithoutRequestEnd(
            @Param("currentDateTime") LocalDateTime currentDateTime);
            
}