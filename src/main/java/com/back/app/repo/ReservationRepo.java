package com.back.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.back.app.model.Reservation;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByBuyerId(Integer buyerId);
    List<Reservation> findByAdvertisementId(Integer AdvertisementId);
    List<Reservation> findByAdvertisementIdIn(List<Integer> advertisementIds);

}