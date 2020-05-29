package com.thesis.webapi.controllers;

import com.thesis.webapi.entities.Station;
import com.thesis.webapi.services.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/secure/stations")
public class StationController {

    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping(value = "/city/{id}")
    public ResponseEntity<List<Station>> getStationsByCityId(@PathVariable("id") UUID cityId) {
        return stationService.getStationsByCityId(cityId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable("id") UUID stationId) {
        return stationService.getStationById(stationId);
    }

}
