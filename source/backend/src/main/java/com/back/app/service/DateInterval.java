package com.back.app.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateInterval {
    private LocalDate startDate;
    private LocalDate endDate;
    
    
    public DateInterval(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    

    public long getDurationInDays() {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
    

    public boolean overlapsWith(DateInterval other) {
        if (startDate == null || endDate == null || 
            other.startDate == null || other.endDate == null) {
            return false;
        }
        return !(endDate.isBefore(other.startDate) || startDate.isAfter(other.endDate));
    }

    public boolean isWithin(DateInterval other) {
        if (startDate == null || endDate == null || 
            other.startDate == null || other.endDate == null) {
            return false;
        }
        return !startDate.isBefore(other.startDate) && !endDate.isAfter(other.endDate);
    }
}