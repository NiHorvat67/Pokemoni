package com.back.app.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerTestRunner {
    
    private final ReservationSchedulerService schedulerService;
    
    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Zagreb")
    public void dailyEarlyMorningCleanup() {
        log.info("=== DAILY TEST FOR RESERVATIONS ===");
        schedulerService.processEndedReservations();
    }
}