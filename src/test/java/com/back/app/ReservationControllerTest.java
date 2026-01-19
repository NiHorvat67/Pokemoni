package com.back.app.controller;

import com.back.app.model.Reservation;
import com.back.app.model.ReservationGrade;
import com.back.app.service.AccountService;
import com.back.app.service.AdvertisementService;
import com.back.app.service.ReservationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AdvertisementService advertisementService;

    private Reservation reservation;

    @BeforeEach
    void setup() {
        reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setBuyerId(10);
        reservation.setAdvertisementId(20);
        reservation.setReservationStart(LocalDateTime.now().minusDays(2));
        reservation.setReservationEnd(LocalDateTime.now().minusDays(1)); // rezervacija završena
    }

    // ocjena 5
    @Test
    void testGradeReservation_RegularCase() throws Exception {
        when(reservationService.getReservationbyId(1)).thenReturn(reservation);

        mockMvc.perform(
                post("/api/reservations/grade/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"grade\":5}")
        )
        .andExpect(status().isOk())
        .andExpect(content().string("Grade 5 saved successfully for reservation 5"));
    }

    // ocjena minimalna - rubni
    @Test
    void testGradeReservation_EdgeCase_MinGrade() throws Exception {
        when(reservationService.getReservationbyId(1)).thenReturn(reservation);

        mockMvc.perform(
                post("/api/reservations/grade/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"grade\":1}")
        )
        .andExpect(status().isOk());
    }

    // nije zavrseno
    @Test
    void testGradeReservation_ThrowsException_NotFinished() throws Exception {
        reservation.setReservationEnd(null);
        when(reservationService.getReservationbyId(1)).thenReturn(reservation);

        mockMvc.perform(
                post("/api/reservations/grade/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"grade\":4}")
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Reservation has not ended yet"));
    }

    // rezervacija ne postoji
    @Test
    void testGradeReservation_NonExistingReservation() throws Exception {
        when(reservationService.getReservationbyId(1)).thenReturn(null);

        mockMvc.perform(
                post("/api/reservations/grade/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"grade\":3}")
        )
        .andExpect(status().isNotFound())
        .andExpect(content().string("Reservation not found with ID: 1"));
    }
}