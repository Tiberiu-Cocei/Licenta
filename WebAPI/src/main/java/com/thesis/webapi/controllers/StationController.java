package com.thesis.webapi.controllers;

import com.thesis.webapi.entities.Station;
import com.thesis.webapi.services.ActivityService;
import com.thesis.webapi.services.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/secure/stations")
public class StationController {

    private final StationService stationService;

    private final ActivityService activityService;

    @Autowired
    public StationController(StationService stationService, ActivityService activityService) {
        this.stationService = stationService;
        this.activityService = activityService;
    }

    @GetMapping(value = "/city/{id}")
    public ResponseEntity<List<Station>> getStationsByCityId(@PathVariable("id") UUID cityId) {
        return stationService.getStationsByCityId(cityId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable("id") UUID stationId) {
        return stationService.getStationById(stationId);
    }

    @PutMapping(value = "/increment-times-clicked-while-empty/{id}")
    public void incrementTimesClickedWhileEmpty(@PathVariable("id") UUID stationId) {
        activityService.incrementTimesClickedWhileEmpty(stationId);
    }

    @PutMapping(value = "/increment-times-clicked-while-full/{id}")
    public void incrementTimesClickedWhileFull(@PathVariable("id") UUID stationId) {
        activityService.incrementTimesClickedWhileFull(stationId);
    }

}
