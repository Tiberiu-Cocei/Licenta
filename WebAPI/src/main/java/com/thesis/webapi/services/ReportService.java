package com.thesis.webapi.services;

import com.thesis.webapi.entities.Report;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ReportService {

    ResponseEntity<Report> getReportById(UUID reportId);

}
