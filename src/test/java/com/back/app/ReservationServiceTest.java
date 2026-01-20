package com.back.app;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.back.app.model.Reservation;
import com.back.app.repo.ReservationRepo;
import com.back.app.service.AdvertisementService;
import com.back.app.service.ReservationService;

public class ReservationServiceTest {

    private ReservationRepo reservationRepo;
    private AdvertisementService advertisementService;
    private ReservationService reservationService;

    private Reservation reservation;

    @BeforeEach
    void setup() {
        reservationRepo = Mockito.mock(ReservationRepo.class);
        advertisementService = Mockito.mock(AdvertisementService.class);

        reservationService = new ReservationService(reservationRepo, advertisementService);

        reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setBuyerId(10);
        reservation.setAdvertisementId(5);
        reservation.setReservationRequestStarted(LocalDateTime.now());
    }
    // TESTOVI ZA METODU saveReservation !!!
    // uspjesna rezervacija
    @Test
    void testSaveReservation_RegularCase() {
        when(reservationRepo.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.saveReservation(reservation);

        assertNotNull(result);
        assertEquals(1, result.getReservationId());
        verify(reservationRepo, times(1)).save(reservation);
    }

    // rubni slucaj - null kraj rezervacije
    @Test
    void testDetermineStatus_EdgeCase_NoEndDate() {
        reservation.setReservationEnd(null);
        String status = reservationService.determineStatus(reservation);

        assertEquals("ACTIVE", status);
    }

    // repo baca exception
    @Test
    void testSaveReservation_ThrowsException() {
        when(reservationRepo.save(any())).thenThrow(new RuntimeException("Database error"));

        Exception e = assertThrows(RuntimeException.class, () -> {
            reservationService.saveReservation(reservation);
        });

        assertEquals("Database error", e.getMessage());
        System.out.println("Greška bačena: " + e.getMessage());
    }

    // poziv nepostojece funkcionalnosti
    @Test
    void testNonExistingFunctionality() {
        when(reservationRepo.findByAdvertisementIdIn(any()))
                .thenThrow(new UnsupportedOperationException("Not implemented"));

        assertThrows(UnsupportedOperationException.class, () -> {
            reservationRepo.findByAdvertisementIdIn(List.of(99));
        });
    }

    // TESTOVI ZA METODU determineStatus !!!
    // end date u buducnosti - redovno
    @Test
    void testDetermineStatus_EndInFuture_Active() {
        Reservation r = new Reservation();
        r.setReservationRequestEnded(LocalDateTime.now().plusDays(3));

        String status = reservationService.determineStatus(r);

        assertEquals("ACTIVE", status);
    }

    // nema end date-a - rubni slucaj - aktivno
    @Test
    void testDetermineStatus_NoEndDate_Active() {
        Reservation r = new Reservation();
        r.setReservationEnd(null);

        String status = reservationService.determineStatus(r);

        assertEquals("ACTIVE", status);
    }

    // end date u proslosti - nije aktivno
    @Test
    void testDetermineStatus_EndInPast_NotActive() {
        Reservation r = new Reservation();
        r.setReservationRequestEnded(LocalDateTime.now().minusDays(1));

        String status = reservationService.determineStatus(r);

        assertEquals("NOT ACTIVE", status);
    }

    // nepostojeca funkcionalnost
    @Test
    void testDetermineStatus_NullReservation_ThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            reservationService.determineStatus(null);
        });
    }
}
