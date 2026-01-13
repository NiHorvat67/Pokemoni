package com.back.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationWithStatusItemName {
    private Reservation reservation;
    private Advertisement advertisement;
    private String status;
}


