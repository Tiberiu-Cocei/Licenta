package com.thesis.webapi.controllers;

import com.thesis.webapi.entities.Report;
import com.thesis.webapi.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
