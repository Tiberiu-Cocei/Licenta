package com.thesis.webapi.services;

import com.thesis.webapi.entities.Activity;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ActivityService {

    ResponseEntity<List<Activity>> getActivitiesByStationId(UUID stationId);

    void incrementTimesClickedWhileEmpty(UUID stationId);

    void incrementTimesClickedWhileFull(UUID stationId);

    void generateRowsForActivity();

    void onScheduleCallGenerateRowsForActivity();

    void onStartupCallGenerateRowsForActivity();

}
