package com.thesis.webapi.services.impl;

import com.thesis.webapi.entities.Bicycle;
import com.thesis.webapi.repositories.BicycleRepository;
import com.thesis.webapi.services.BicycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BicycleServiceImpl implements BicycleService {

    private final BicycleRepository bicycleRepository;

    @Autowired
    public BicycleServiceImpl(BicycleRepository bicycleRepository) {
        this.bicycleRepository = bicycleRepository;
    }

    @Override
    public ResponseEntity<List<Bicycle>> getBicyclesByStationId(UUID stationId) {
        return new ResponseEntity<>(bicycleRepository.getBicyclesByStationId(stationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Bicycle> getBicycleById(UUID bicycleId) {
        Bicycle bicycle = bicycleRepository.getBicycleById(bicycleId);
        if(bicycle != null) {
            return new ResponseEntity<>(bicycle, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
