package com.back.app.controller;

import java.time.LocalDateTime;
import java.util.LinkedList;
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

import com.back.app.model.Account;
import com.back.app.model.Advertisement;
import com.back.app.model.Reservation;
import com.back.app.model.ReservationWithStatusBuyerItemName;
import com.back.app.model.ReservationWithStatusItemName;
import com.back.app.service.AccountService;
import com.back.app.service.AdvertisementService;
import com.back.app.service.ReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.stream.Collectors;

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

    private final ReservationService reservationService;
    private final AccountService accountService;
    private final AdvertisementService advertisementService;

    @Operation(summary = "Retrieve all item types", description = "Returns all Reservations")
    @GetMapping("/")
    public ResponseEntity<List<Reservation>> getAllTypeName() {
        return ResponseEntity.ok().body(reservationService.getAllReservations());
    }

    @Operation(summary = "Retrieve Reservations by ID", description = "Returns the details for a specific Reservation ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getTypeIdName(@PathVariable Integer id) {
        return ResponseEntity.ok().body(reservationService.getReservationbyId(id));
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

            reservationService.saveReservation(reservation);
            
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

            Reservation reservation = reservationService.getReservationbyId(id);
            
            if (reservation == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Reservation not found with ID: " + id);
            }
            reservation.setReservationEnd(LocalDateTime.now());

            reservationService.updateReservation(id, reservation);

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
            Reservation reservation = reservationService.getReservationbyId(id);

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
            reservationService.updateReservation(id, reservation);

            return ResponseEntity.ok().body("Grade " + grade1 + " saved successfully for reservation " + grade1);

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Get reservations by trader ID", description = "Retrieves all reservations for a trader with status (ACTIVE/NOT ACTIVE)")
    @GetMapping("/trader/{id}")
    public ResponseEntity<List<ReservationWithStatusBuyerItemName>> getTradersReservation(@PathVariable Integer id) {
        try {
            log.info("Retrieving reservations for trader ID: {}", id);
            
            List<Reservation> reservations = reservationService.getReservationsByTraderId(id);

            
            List<Account> buyers = reservations.stream()
            .map(reservation -> {
                return accountService.getUserbyId(reservation.getBuyerId());
            }).collect(Collectors.toList());
            
            List<Advertisement> advertisements = reservations.stream()
            .map(reservation -> {
                return advertisementService.getAdvertisementbyId(reservation.getAdvertisementId());
            }).collect(Collectors.toList());


            List<ReservationWithStatusBuyerItemName> reservationWithContext = new LinkedList<>();

            for(int i = 0; i < reservations.size(); i++){
                reservationWithContext.add(
                    new ReservationWithStatusBuyerItemName(
                        reservations.get(i), buyers.get(i), advertisements.get(i), 
                        reservationService.determineStatus(reservations.get(i)) 
                    )
                );
            }


            
            return ResponseEntity.ok().body(reservationWithContext);
            
            
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get reservations by buyer ID", description = "Retrieves all reservations for a buyer with status (ACTIVE/NOT ACTIVE)")
    @GetMapping("/buyer/{id}")
    public ResponseEntity<List<ReservationWithStatusItemName>> getBuyersReservation(@PathVariable Integer id) {
        try {
            log.info("Retrieving reservations for trader ID: {}", id);
            
            List<Reservation> reservations = reservationService.getReservationsByBuyerId(id);
            
            List<Advertisement> advertisements = reservations.stream()
            .map(reservation -> {
                return advertisementService.getAdvertisementbyId(reservation.getAdvertisementId());
            }).collect(Collectors.toList());


            List<ReservationWithStatusItemName> reservationWithContext = new LinkedList<>();

            for(int i = 0; i < reservations.size(); i++){
                reservationWithContext.add(
                    new ReservationWithStatusItemName(
                        reservations.get(i), advertisements.get(i), 
                        reservationService.determineStatus(reservations.get(i)) 
                    )
                );
            }


            
            return ResponseEntity.ok().body(reservationWithContext);
            
            
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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