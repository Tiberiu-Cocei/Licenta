package com.thesis.webapi.services;

import com.thesis.webapi.entities.Bicycle;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface BicycleService {

    ResponseEntity<List<Bicycle>> getBicyclesByStationId(UUID stationId);

    ResponseEntity<Bicycle> getBicycleById(UUID bicycleId);

    void clearStationsOfLateBicycles();

    void onScheduleCallClearStationsOfLateBicycles();

}
