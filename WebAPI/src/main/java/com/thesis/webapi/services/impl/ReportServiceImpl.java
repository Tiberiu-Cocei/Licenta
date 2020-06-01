package com.thesis.webapi.services.impl;

import com.thesis.webapi.dtos.ReportCreateDto;
import com.thesis.webapi.entities.Report;
import com.thesis.webapi.repositories.ReportRepository;
import com.thesis.webapi.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public ResponseEntity<Report> getReportById(UUID reportId) {
        Report report = reportRepository.getReportById(reportId);
        if(report != null) {
            return new ResponseEntity<>(report, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<Report>> getReportsByUserId(UUID userId) {
        return new ResponseEntity<>(reportRepository.getReportsByUserId(userId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> saveReport(ReportCreateDto reportCreateDto) {
        reportRepository.save(new Report(reportCreateDto));
        return new ResponseEntity<>("Successfully created report.", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<Report>> getReportsByBicycleId(UUID bicycleId) {
        return new ResponseEntity<>(reportRepository.getBicycleReports(bicycleId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getReportCountByBicycleId(UUID bicycleId) {
        return new ResponseEntity<>(reportRepository.getBicycleReportCount(bicycleId), HttpStatus.OK);
    }
}
