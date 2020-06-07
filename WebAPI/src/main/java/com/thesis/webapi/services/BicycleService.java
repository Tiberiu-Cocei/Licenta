package com.thesis.webapi.services;

import com.thesis.webapi.dtos.BicycleStatusDto;
import com.thesis.webapi.entities.Bicycle;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface BicycleService {

    ResponseEntity<List<Bicycle>> getBicyclesByStationId(UUID stationId);

    ResponseEntity<Bicycle> getBicycleById(UUID bicycleId);

    ResponseEntity<BicycleStatusDto> getBicycleCountByStatus(UUID stationId);

    ResponseEntity<List<Bicycle>> getBicyclesWithLimitAndOffset(Integer limit, Integer offset, UUID stationId);

    void clearStationsOfLateBicycles();

    void onScheduleCallClearStationsOfLateBicycles();

}
