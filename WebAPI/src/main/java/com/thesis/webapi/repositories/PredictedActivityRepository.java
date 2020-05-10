package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.PredictedActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.UUID;

public interface PredictedActivityRepository extends JpaRepository<PredictedActivity, UUID> {

    PredictedActivity getPredictedActivityByStationIdAndDayAndHour(UUID stationId, Date day, int hour);

}
