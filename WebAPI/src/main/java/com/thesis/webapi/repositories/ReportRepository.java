package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {

    Report getReportById(UUID reportId);

    List<Report> getReportsByUserId(UUID userId);

    @Query(value = "SELECT u FROM Report u WHERE u.bicycleId = :bicycleId AND u.reviewed = false")
    List<Report> getBicycleReports(@Param("bicycleId") UUID bicycleId);

    @Query(value = "SELECT COUNT(u) FROM Report u WHERE u.bicycleId = :bicycleId AND u.reviewed = false")
    Integer getBicycleReportCount(@Param("bicycleId") UUID bicycleId);

}
