package com.thesis.webapi.services;

import com.thesis.webapi.entities.Discount;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface DiscountService {

    ResponseEntity<List<Discount>> getDiscountsByStationAndTime(UUID stationId);

    void bringStationBicycleNumbersToPredictedValues();

    void onScheduleCallBringStationBicycleNumbersToPredictedValues();

}
