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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisement_id")
    private Integer advertisementId;
    
    @Column(name = "advertisement_price")
    private BigDecimal advertisementPrice;
    
    @Column(name = "advertisement_deposit")
    private BigDecimal advertisementDeposit;
    
    @Column(name = "advertisement_location_takeover")
    private String advertisementLocationTakeover;
    
    @Column(name = "advertisement_location_return")
    private String advertisementLocationReturn;
    
    @Column(name = "advertisement_start")
    private LocalDate advertisementStart;
    
    @Column(name = "advertisement_end")
    private LocalDate advertisementEnd;
    
    @Column(name = "trader_id")
    private Integer traderId;
    
    @Column(name = "reservation_id")
    private Integer reservationId;
    
    @Column(name = "itemtype_id")
    private Integer itemtypeId;
    
    @Column(name = "item_description")
    private String itemDescription;
    
    @Column(name = "item_image_path")
    private String itemImagePath;
    
}