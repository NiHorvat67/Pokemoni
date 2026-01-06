package com.back.app.service;

import com.back.app.model.Report;
import com.back.app.repo.ReportRepo;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Objects;

@Service
public class ReportService {

    private final ReportRepo reportRepo;

    public ReportService(ReportRepo reportRepo) {
        this.reportRepo = reportRepo;
    }

    public Report createReport(Report report) {
        Objects.requireNonNull(report, "Report object cannot be null");
        
        // Ensure the ID is null so the database SERIAL/IDENTITY handles generation
        report.setReport_id(null);

        // Basic validation for required fields
        Objects.requireNonNull(report.getReporter_id(), "Reporter ID must be provided");
        Objects.requireNonNull(report.getReported_id(), "Reported user ID must be provided");

        if (report.getReport_status() == null) {
            report.setReport_status("pending");
        }
        
        return reportRepo.save(report);
    }

    public Report getReportById(Integer id) {
        Objects.requireNonNull(id, "Report ID cannot be null");
        
        return reportRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));
    }

    public List<Report> getAllReports() {
        return reportRepo.findAll();
    }

    public Report updateReport(Integer id, Report reportDetails) {
        Objects.requireNonNull(id, "Report ID cannot be null");
        Objects.requireNonNull(reportDetails, "Report details cannot be null");

        Report existingReport = reportRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));

        if (reportDetails.getReport_details() != null) {
            existingReport.setReport_details(reportDetails.getReport_details());
        }
        
        if (reportDetails.getReport_status() != null) {
            existingReport.setReport_status(reportDetails.getReport_status());
        }

        return reportRepo.save(existingReport);
    }
    
    public void deleteReport(Integer id) {
        Objects.requireNonNull(id, "Report ID cannot be null");
        
        if (!reportRepo.existsById(id)) {
            throw new RuntimeException("Report not found with id: " + id);
        }
        reportRepo.deleteById(id);
    }
}