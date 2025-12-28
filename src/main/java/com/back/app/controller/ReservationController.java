package com.back.app.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.back.app.model.Reservation;
import com.back.app.service.ReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Reservations", description = "Operations related to reservations")
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ReservationController {

    private final ReservationService ReservationService;

    @Operation(summary = "Retrieve all item types", description = "Returns all Reservations")
    @GetMapping("/")
    public ResponseEntity<List<Reservation>> getAllTypeName() {
        return ResponseEntity.ok().body(ReservationService.getAllReservations());
    }

    @Operation(summary = "Retrieve Reservations by ID", description = "Returns the details for a specific Reservation ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getTypeIdName(@PathVariable Integer id) {
        return ResponseEntity.ok().body(ReservationService.getReservationbyId(id));
    }

    // buyer creates reservation, trader ends it when the gear is returned and then
    // buyer grades reservation
    @Operation(summary = "Create new reservation", description = "Creates new reservation")
    @PostMapping("/create")
    public ResponseEntity<String> reserve(@RequestBody String newReservationString) {
        try {
            log.info("Received Reservation: {}", newReservationString);

            Reservation reservation = Reservation.convertToReservation(newReservationString);
            reservation.setReservationStart(LocalDateTime.now());

            ReservationService.saveReservation(reservation);
            
            return ResponseEntity.ok().body(reservation.getReservationId().toString());

        } catch (JsonProcessingException e) {
            log.error("JSON parsing error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

                    .body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "End reservation", description = "Ends reservation")
    @PostMapping("/end/{id}")
    public ResponseEntity<String> endReservation(@PathVariable Integer id) {
        try {
            log.info("Received Reservation: {}", id);

            Reservation reservation = ReservationService.getReservationbyId(id);
            
            if (reservation == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Reservation not found with ID: " + id);
            }
            reservation.setReservationEnd(LocalDateTime.now());

            ReservationService.updateReservation(id, reservation);

            return ResponseEntity.ok().body(reservation.getReservationId().toString());

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

                    .body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Grade reservation", description = "Grade reservation after it ends")
    @PostMapping("/grade/{id}")
    public ResponseEntity<String> gradeReservation(@PathVariable Integer id,
            @RequestBody String grade) {
        try {
            log.info("Received Reservation: {}", id);
            Reservation reservation = ReservationService.getReservationbyId(id);

            if (reservation == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Reservation not found with ID: " + id);
            }

            if (reservation.getReservationEnd() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Reservation has not ended yet");
            }

            Integer grade1 = parseGrade(grade.trim());
            if (grade1 == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid grade format. Must be an integer between 1-5");
            }
            if (grade1 < 1 || grade1 > 5) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Grade must be between 1 and 5");
            }

            reservation.setReservationGrade(grade1);
            ReservationService.updateReservation(id, reservation);

            return ResponseEntity.ok().body("Grade " + grade1 + " saved successfully for reservation " + grade1);

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    private Integer parseGrade(String gradeString) {
        try {
            return Integer.parseInt(gradeString.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

}