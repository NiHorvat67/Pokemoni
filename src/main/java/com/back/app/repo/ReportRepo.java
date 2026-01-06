package com.back.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.back.app.model.Report;

public interface ReportRepo extends JpaRepository<Report, Integer>{
    
}
