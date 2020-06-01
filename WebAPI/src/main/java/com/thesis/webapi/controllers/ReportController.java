package com.thesis.webapi.controllers;

import com.thesis.webapi.dtos.ReportCreateDto;
import com.thesis.webapi.entities.Report;
import com.thesis.webapi.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/secure/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable("id") UUID reportId) {
        return reportService.getReportById(reportId);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<Report>> getReportsByUserId(@PathVariable("id") UUID userId) {
        return reportService.getReportsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<String> saveReport(@Valid @RequestBody ReportCreateDto reportCreateDto) {
        return reportService.saveReport(reportCreateDto);
    }

    @GetMapping(value = "/bicycle/{id}")
    public ResponseEntity<List<Report>> getReportsByBicycleId(@PathVariable("id") UUID bicycleId) {
        return reportService.getReportsByBicycleId(bicycleId);
    }

    @GetMapping(value = "/count/{id}")
    public ResponseEntity<Integer> getReportCountByBicycleId(@PathVariable("id") UUID bicycleId) {
        return reportService.getReportCountByBicycleId(bicycleId);
    }

}
