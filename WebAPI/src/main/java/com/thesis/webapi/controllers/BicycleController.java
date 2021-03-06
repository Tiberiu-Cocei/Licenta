package com.thesis.webapi.controllers;

import com.thesis.webapi.dtos.BicycleStatusDto;
import com.thesis.webapi.entities.Bicycle;
import com.thesis.webapi.services.BicycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api")
public class BicycleController {

    private final BicycleService bicycleService;

    @Autowired
    public BicycleController(BicycleService bicycleService) {
        this.bicycleService = bicycleService;
    }

    @GetMapping(value = "/secure/bicycles/station/{id}")
    public ResponseEntity<List<Bicycle>> getBicyclesByStationId(@PathVariable("id") UUID stationId) {
        return bicycleService.getBicyclesByStationId(stationId);
    }

    @GetMapping(value = "/secure/bicycles/{id}")
    public ResponseEntity<Bicycle> getBicycleById(@PathVariable("id") UUID bicycleId) {
        return bicycleService.getBicycleById(bicycleId);
    }

    @GetMapping(value = "/admin/bicycles/status-numbers/")
    public ResponseEntity<BicycleStatusDto> getBicycleCountByStatus(@RequestParam(value = "id", required = false) UUID stationId) {
        return bicycleService.getBicycleCountByStatus(stationId);
    }

    @GetMapping(value = "/admin/bicycles")
    public ResponseEntity<List<Bicycle>> getBicyclesWithLimitAndOffset(
            @RequestParam Integer limit, @RequestParam Integer offset, @RequestParam(value = "id", required = false) UUID stationId) {
        return bicycleService.getBicyclesWithLimitAndOffset(limit, offset, stationId);
    }

}
