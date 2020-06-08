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

    @GetMapping(value = "/activities/")
    public ResponseEntity<List<Activity>> getActivitiesByStationId(
            @RequestParam Integer limit, @RequestParam Integer offset, @RequestParam(value = "id") UUID stationId) {
        return activityService.getActivitiesByStationIdWithLimitAndOffset(stationId, limit, offset);
    }

    @GetMapping(value = "/predicted-activities/")
    public ResponseEntity<List<PredictedActivity>> getPredictedActivitiesByStationId(
            @RequestParam Integer limit, @RequestParam Integer offset, @RequestParam(value = "id") UUID stationId) {
        return predictedActivityService.getPredictedActivitiesByStationIdWithLimitAndOffset(stationId, limit, offset);
    }

}
