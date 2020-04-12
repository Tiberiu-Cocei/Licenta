package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {

    Report getReportById(UUID reportId);

}
