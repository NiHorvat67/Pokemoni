package com.back.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import com.back.app.model.Advertisement;
import com.back.app.repo.AdverNoJoinRepo;
import com.back.app.repo.AdvertisementRepo;

public class AdvertisementServiceTest {

    private AdvertisementRepo advertisementRepo;
    private AdverNoJoinRepo adverNoJoinRepo; 
    private AdvertisementService advertisementService;

    private Advertisement ad1;
    private Advertisement ad2;

    @BeforeEach
    void setup() {
        advertisementRepo = Mockito.mock(AdvertisementRepo.class);
        adverNoJoinRepo = Mockito.mock(AdverNoJoinRepo.class);

        advertisementService = new AdvertisementService(advertisementRepo, adverNoJoinRepo);

        ad1 = new Advertisement();
        ad1.setAdvertisementId(1);
        ad1.setItemName("Football ball");

        ad2 = new Advertisement();
        ad2.setAdvertisementId(2);
        ad2.setItemName("Tennis Racket");
    }

    // redovni slucaj
    @Test
    void testFilterAdvertisements_RegularCase() {
        when(advertisementRepo.findFilteredAdvertisements(
                anyString(), any(), any(), any(), any(), any()
        )).thenReturn(List.of(ad1, ad2));

        List<Advertisement> result = advertisementService.getFilteredAdvertisements(
                "Foot", 
                3,
                LocalDate.now().minusDays(10),
                LocalDate.now().plusDays(10),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(150)
        );

        assertEquals(2, result.size());
        assertEquals("Football ball", result.get(0).getItemName());
    }

    // rubni slucaj -svi filteri null
    @Test
    void testFilterAdvertisements_EdgeCase_AllNull() {
        when(advertisementRepo.findFilteredAdvertisements(
                isNull(), isNull(), isNull(), isNull(), isNull(), isNull()
        )).thenReturn(List.of(ad1));

        List<Advertisement> result = advertisementService.getFilteredAdvertisements(
                null, null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals("Football ball", result.get(0).getItemName());
    }

    // izazivanje pogreske iz repozitorija
    @Test
    void testFilterAdvertisements_ThrowsException() {
        when(advertisementRepo.findFilteredAdvertisements(any(), any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Database error"));

        Exception e = assertThrows(RuntimeException.class, () -> {
            advertisementService.getFilteredAdvertisements(
                    "Shoes", 1, LocalDate.now(), LocalDate.now(),
                    BigDecimal.TEN, BigDecimal.ONE
            );
        });

        assertEquals("Database error", e.getMessage());
    }

    // poziv nepostojece funkcionalnosti
    @Test
    void testNonExistingFunctionality() {
        when(advertisementRepo.findByAdvertisementId(any()))
                .thenThrow(new UnsupportedOperationException("Not implemented"));

        assertThrows(UnsupportedOperationException.class, () -> {
            advertisementRepo.findByAdvertisementId(999);
        });
    }
}