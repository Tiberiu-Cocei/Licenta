package com.thesis.webapi.controllers;

import com.thesis.webapi.entities.Bicycle;
import com.thesis.webapi.services.BicycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/secure/bicycles")
public class BicycleController {

    private final BicycleService bicycleService;

    @Autowired
    public BicycleController(BicycleService bicycleService) {
        this.bicycleService = bicycleService;
    }

    @GetMapping(value = "/station/{id}")
    public ResponseEntity<List<Bicycle>> getBicyclesByStationId(@PathVariable("id") UUID stationId) {
        return bicycleService.getBicyclesByStationId(stationId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Bicycle> getBicycleById(@PathVariable("id") UUID bicycleId) {
        return bicycleService.getBicycleById(bicycleId);
    }

}
