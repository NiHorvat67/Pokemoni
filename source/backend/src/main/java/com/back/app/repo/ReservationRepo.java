package com.back.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.back.app.model.Reservation;
import java.util.List;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByBuyerId(Integer buyerId);
    
    List<Reservation> findByAdvertisementIdIn(List<Integer> advertisementIds);

}