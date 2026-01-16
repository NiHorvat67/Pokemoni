package com.back.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer reservationId;

    @Column(name = "reservation_start", nullable = false)
    private LocalDate reservationStart;

    @Column(name = "reservation_end", nullable = false)
    private LocalDate reservationEnd;

    @Column(name = "reservation_request_started", nullable = false)
    private LocalDateTime reservationRequestStarted;

    @Column(name = "reservation_request_ended")
    private LocalDateTime reservationRequestEnded;

    @Column(name = "reservation_grade")
    private Integer reservationGrade;

    @Column(name = "buyer_id")
    private Integer buyerId;

    @Column(name = "advertisement_id")
    private Integer advertisementId;

    public Reservation() {
    }

    public static Reservation convertToReservation(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(jsonString, Reservation.class);
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getReservationStart() {
        return reservationStart;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getReservationEnd() {
        return reservationEnd;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime getReservationRequestStarted() {
        return reservationRequestStarted;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime getReservationRequestEnded() {
        return reservationRequestEnded;
    }
}