package com.thesis.webapi.services.impl;

import com.thesis.webapi.entities.PredictedActivity;
import com.thesis.webapi.repositories.PredictedActivityRepository;
import com.thesis.webapi.services.PredictedActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PredictedActivityServiceImpl implements PredictedActivityService {

    private final PredictedActivityRepository predictedActivityRepository;

    @Autowired
    public PredictedActivityServiceImpl(PredictedActivityRepository predictedActivityRepository) {
        this.predictedActivityRepository = predictedActivityRepository;
    }

    @Override
    public ResponseEntity<List<PredictedActivity>> getPredictedActivitiesByStationId(UUID stationId) {
        return new ResponseEntity<>(
                predictedActivityRepository.getPredictedActivityByStationIdAndDay(stationId, new Date()), HttpStatus.OK);
    }
}
