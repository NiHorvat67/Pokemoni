package com.back.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.back.app.model.Reservation;



public interface ReservationRepo extends JpaRepository<Reservation, Integer> {


  

}