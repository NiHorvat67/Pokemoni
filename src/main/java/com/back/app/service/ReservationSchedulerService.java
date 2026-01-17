package com.back.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.back.app.model.Report;
import com.back.app.model.Reservation;
import com.back.app.repo.ReservationRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationSchedulerService {

    private final ReservationService reservationService;
    private final ReportService reportService;
    private final ReservationRepo reservationRepo;



    @Transactional
    public void processEndedReservations() {
        LocalDateTime now = LocalDateTime.now();
        log.debug("Running reservation end checker at {}", now);

        List<Reservation> endedReservations = reservationRepo.findEndedReservationsWithoutRequestEnd(now);

        if (endedReservations.isEmpty()) {
            log.debug("No ended reservations found to process");
            return;
        }

        log.info("Found {} ended reservations to process", endedReservations.size());

        for (Reservation reservation : endedReservations) {
            try {
                Integer reservationId = reservation.getReservationId();
                String report_details="AUTOMATIC REPORT";
                Report report=  new Report(reservationService.getTraderIdForReservationId(reservationId), reservation.getBuyerId(), report_details,"pending", now);
                reservation.setReservationRequestEnded(now);


                log.info("Processed ended reservation {}: Set request_ended to {}",
                        reservationId, now);

                reservationService.saveReservation(reservation);
                reportService.saveReport(report);
            } catch (Exception e) {
                log.error("Failed to process reservation {}: {}",
                        reservation.getReservationId(), e.getMessage(), e);
            }
        }
    }

    /**
     * Optional: Run more frequently for testing
     */
    @Scheduled(fixedRate = 30000) // Every 30 seconds (for testing)
    public void processEndedReservationsFrequent() {
        // Same logic, but more frequent
        processEndedReservations();
    }
}