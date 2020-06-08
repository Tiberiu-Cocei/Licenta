package com.thesis.webapi.services;

import com.thesis.webapi.entities.PredictedActivity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PredictedActivityService {

    ResponseEntity<List<PredictedActivity>> getPredictedActivitiesByStationIdWithLimitAndOffset(UUID stationId, int limit, int offset);

}
