package com.back.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "advertisement")
public class Advertisement {

    @Id
    private Integer advertisement_id;
    private BigDecimal advertisementPrice;
    private BigDecimal advertisementDeposit;
    private String advertisementLocationTakeover;
    private String advertisementLocationReturn;
    private LocalDate advertisementStart;
    private LocalDate advertisementEnd;
    private Integer traderId;
    private Integer reservationId;
}