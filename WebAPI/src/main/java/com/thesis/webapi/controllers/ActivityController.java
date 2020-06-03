package com.thesis.webapi.controllers;

import com.thesis.webapi.entities.Activity;
import com.thesis.webapi.entities.PredictedActivity;
import com.thesis.webapi.services.ActivityService;
import com.thesis.webapi.services.PredictedActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/admin")
public class ActivityController {

    private final ActivityService activityService;

    private final PredictedActivityService predictedActivityService;

    @Autowired
    public ActivityController(ActivityService activityService, PredictedActivityService predictedActivityService) {
        this.activityService = activityService;
        this.predictedActivityService = predictedActivityService;
    }

    @GetMapping(value = "/activities/station/{id}")
    public ResponseEntity<List<Activity>> getActivitiesByStationId(@PathVariable("id") UUID stationId) {
        return activityService.getActivitiesByStationId(stationId);
    }

    @GetMapping(value = "/predicted-activities/station/{id}")
    public ResponseEntity<List<PredictedActivity>> getPredictedActivitiesByStationId(@PathVariable("id") UUID stationId) {
        return predictedActivityService.getPredictedActivitiesByStationId(stationId);
    }

}
