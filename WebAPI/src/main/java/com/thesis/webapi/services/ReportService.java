package com.thesis.webapi.services;

import com.thesis.webapi.dtos.ReportCreateDto;
import com.thesis.webapi.entities.Report;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ReportService {

    ResponseEntity<Report> getReportById(UUID reportId);

    ResponseEntity<List<Report>> getReportsByUserId(UUID userId);

    ResponseEntity<String> saveReport(ReportCreateDto reportCreateDto);

    ResponseEntity<List<Report>> getReportsByBicycleId(UUID bicycleId);

    ResponseEntity<Integer> getReportCountByBicycleId(UUID bicycleId);

}
