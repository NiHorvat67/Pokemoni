package com.back.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@AllArgsConstructor
@Data
@Entity
@Table(name = "advertisement")

public class AdverNoJoin {

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

    @Column(name = "item_name")
    private String itemName;


    @Column(name = "itemtype_id")
    private Integer itemTypeId;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "item_image_path")
    private String itemImagePath;

    // placeholder
    public AdverNoJoin() {
        this.advertisementPrice = new BigDecimal("25.00");
        this.advertisementDeposit = new BigDecimal("100.00");
        this.advertisementLocationTakeover = "London Sports Center";
        this.advertisementLocationReturn = "London Sports Center";
        this.advertisementStart = LocalDate.now().plusDays(1);
        this.advertisementEnd = LocalDate.now().plusMonths(3);
        this.reservationId = null;
        this.itemTypeId=3;
        this.traderId=3;
        this.itemName = "Professional Skis Set";
        this.itemDescription = "High-quality professional skis with poles, perfect for intermediate to advanced skiers.";
        this.itemImagePath = "/images/jhdfsghfdhgkds1.jpg";

    }

    public static AdverNoJoin convertToAdverNoJoin(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); 
        return objectMapper.readValue(jsonString, AdverNoJoin.class);
    }
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getAdvertisementStart() {
        return advertisementStart;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getAdvertisementEnd() {
        return advertisementEnd;
    }
}