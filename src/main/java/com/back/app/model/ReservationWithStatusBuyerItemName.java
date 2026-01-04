package com.back.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationWithStatusBuyerItemName {
    private Reservation reservation;
    private Account buyer;
    private Advertisement advertisement;
    private String status;
}

