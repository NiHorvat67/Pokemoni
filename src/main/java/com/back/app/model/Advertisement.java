package com.back.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trader_id")
    private Account trader;

    @Column(name = "reservation_id")
    private Integer reservationId;

    @Column(name = "item_name")
    private String itemName;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemtype_id")
    private ItemType itemType;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "item_image_path")
    private String itemImagePath;

    // placeholder
    public Advertisement() {
        this.advertisementPrice = new BigDecimal("25.00");
        this.advertisementDeposit = new BigDecimal("100.00");
        this.advertisementLocationTakeover = "London Sports Center";
        this.advertisementLocationReturn = "London Sports Center";
        this.advertisementStart = LocalDate.now().plusDays(1);
        this.advertisementEnd = LocalDate.now().plusMonths(3);
        this.reservationId = null;
        this.itemName = "Professional Skis Set";
        this.itemDescription = "High-quality professional skis with poles, perfect for intermediate to advanced skiers.";
        this.itemImagePath = "/images/jhdfsghfdhgkds1.jpg";

    }

    public static Advertisement convertToAdvertisement(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, Advertisement.class);
    }
}