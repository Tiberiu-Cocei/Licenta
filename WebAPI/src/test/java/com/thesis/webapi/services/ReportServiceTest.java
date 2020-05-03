package com.thesis.webapi.services;

import com.thesis.webapi.dtos.ReportCreateDto;
import com.thesis.webapi.entities.Report;
import com.thesis.webapi.repositories.ReportRepository;
import com.thesis.webapi.services.impl.ReportServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReportServiceTest {

    @Mock
    ReportRepository reportRepository;

    @InjectMocks
    ReportServiceImpl reportService;

    private UUID userId;

    private ArrayList<Report> reportList;

    private ReportCreateDto reportCreateDto;

    private Report report;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        userId = UUID.randomUUID();
        Report firstReport = new Report(userId, "First");
        Report secondReport = new Report(userId, "Second");
        reportCreateDto = new ReportCreateDto("Save test");
        report = new Report(reportCreateDto);
        reportList = new ArrayList<>();
        reportList.add(firstReport);
        reportList.add(secondReport);
    }

    @Test
    public void whenGetReportsByUserIdIsCalled_WithExistingId_ThenReturnCorrectList() {
        //Arrange
        Mockito.when(reportRepository.getReportsByUserId(userId)).thenReturn(reportList);

        //Act
        ResponseEntity<List<Report>> reports = reportService.getReportsByUserId(userId);

        //Assert
        Assertions.assertThat(reports).isNotNull();
        Assertions.assertThat(reports.getBody().size()).isEqualTo(2);
        Assertions.assertThat(reports.getBody().get(0)).isEqualToComparingFieldByFieldRecursively(reportList.get(0));
        Assertions.assertThat(reports.getBody().get(1)).isEqualToComparingFieldByFieldRecursively(reportList.get(1));
        Assertions.assertThat(reports.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenGetReportsByUserIdIsCalled_WithNonexistentId_ThenReturnEmptyList() {
        //Arrange
        Mockito.when(reportRepository.getReportsByUserId(userId)).thenReturn(reportList);

        //Act
        ResponseEntity<List<Report>> reports = reportService.getReportsByUserId(UUID.randomUUID());

        //Assert
        Assertions.assertThat(reports).isNotNull();
        Assertions.assertThat(reports.getBody().size()).isEqualTo(0);
        Assertions.assertThat(reports.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenSaveReportIsCalled_WithValidReport_ThenGetCorrectResponse() {
        //Arrange

        //Act
        ResponseEntity<String> result = reportService.saveReport(reportCreateDto);

        //Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getBody()).isEqualTo("Successfully created report.");
        Assertions.assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    }

    @Test
    public void whenGetReportByIdIsCalled_WithExistingId_ThenReturnCorrectReport() {
        //Arrange
        Mockito.when(reportRepository.getReportById(this.report.getId())).thenReturn(this.report);

        //Act
        ResponseEntity<Report> report = reportService.getReportById(this.report.getId());

        //Assert
        Assertions.assertThat(report).isNotNull();
        Assertions.assertThat(report.getBody()).isEqualToComparingFieldByFieldRecursively(this.report);
        Assertions.assertThat(report.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenGetReportByIdIsCalled_WithNonexistentId_ThenError() {
        //Arrange

        //Act
        ResponseEntity<Report> report = reportService.getReportById(UUID.randomUUID());

        //Assert
        Assertions.assertThat(report).isNotNull();
        Assertions.assertThat(report.getBody()).isNull();
        Assertions.assertThat(report.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    }

    @After
    public void tearDown(){
        userId = null;
        reportList = null;
        reportCreateDto = null;
        report = null;
    }

}
