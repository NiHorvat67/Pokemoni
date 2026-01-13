package com.back.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.back.app.model.Report;
import com.back.app.model.ReportAccountsTimestamp;
import com.back.app.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Reports", description = "Manages user reports, including creation, retrieval, and status updates.")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Retrieve all reports", description = "Returns a list of all reports in the system. Typically used by admins.")
    @Secured("ROLE_ADMIN")
    @GetMapping("/")
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok().body(reportService.getAllReports());
    }

    @Operation(summary = "Retrieve all reports with account", description = "Returns a list of all reports in the system. Typically used by admins.")
    @Secured("ROLE_ADMIN")
    @GetMapping("/with-accounts/")
    public ResponseEntity<List<ReportAccountsTimestamp>> getAllReportsWithAccounts() {
        return ResponseEntity.ok().body(reportService.getAllReportsWithAccounts());
    }


    @Operation(summary = "Retrieve report by ID", description = "Returns details of a specific report.")
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(reportService.getReportById(id));
        } catch (Exception e) {
            log.error("Error retrieving report: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Create a new report", description = "Submits a new report regarding a user.")
    @Secured({ "ROLE_TRADER", "ROLE_BUYER", "ROLE_ADMIN" })
    @PostMapping("/create")
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        try {
            Report savedReport = reportService.createReport(report);
            log.info("Report created successfully with ID: {}", savedReport.getReport_id());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
        } catch (Exception e) {
            log.error("Unexpected error creating report: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Update report status or details", description = "Updates the status or details of an existing report.")
    @Secured("ROLE_ADMIN")
    @PostMapping("/update/{id}")
    public ResponseEntity<Report> updateReport(@PathVariable Integer id, @RequestBody Report reportDetails) {
        try {
            Report updatedReport = reportService.updateReport(id, reportDetails);
            return ResponseEntity.ok().body(updatedReport);
        } catch (Exception e) {
            log.error("Error updating report ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Delete a report", description = "Removes a report from the system.")
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable Integer id) {
        try {
            reportService.deleteReport(id);
            return ResponseEntity.ok().body("Successfully deleted report with id " + id);
        } catch (Exception e) {
            log.error("Error deleting report: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting report with id " + id);
        }
    }
}