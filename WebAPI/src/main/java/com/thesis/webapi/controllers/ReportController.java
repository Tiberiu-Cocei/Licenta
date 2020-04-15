package com.thesis.webapi.controllers;

import com.thesis.webapi.dtos.ReportCreateDto;
import com.thesis.webapi.entities.Report;
import com.thesis.webapi.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/reports")
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
    public ResponseEntity<String> saveReport(@RequestBody ReportCreateDto reportCreateDto) {
        return reportService.saveReport(reportCreateDto);
    }

}
