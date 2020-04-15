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
        if(reportCreateDto.getSeverity() <= 1 || reportCreateDto.getSeverity() > 10)
            return new ResponseEntity<>("Severity must be greater or equal to 1 and less or equal to 10.", HttpStatus.BAD_REQUEST);
        if(reportCreateDto.getDescription() == null || reportCreateDto.getDescription().length() < 10)
            return new ResponseEntity<>("Description must have at least 10 characters.", HttpStatus.BAD_REQUEST);
        if(reportCreateDto.getUserId() == null || reportCreateDto.getBicycleId() == null)
            return new ResponseEntity<>("Ids must not be null.", HttpStatus.BAD_REQUEST);
        reportRepository.save(new Report(reportCreateDto));
        return new ResponseEntity<>("Successfully created report.", HttpStatus.CREATED);
    }

}
