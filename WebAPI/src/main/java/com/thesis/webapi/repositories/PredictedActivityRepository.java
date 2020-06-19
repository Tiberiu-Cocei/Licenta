package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.PredictedActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PredictedActivityRepository extends JpaRepository<PredictedActivity, UUID> {

    PredictedActivity getPredictedActivityByStationIdAndDayAndHour(UUID stationId, Date day, int hour);

    List<PredictedActivity> getPredictedActivityByStationIdAndDay(UUID stationId, Date day);

    List<PredictedActivity> getPredictedActivityByStationId(UUID stationId);

    @Query(value = "SELECT * FROM Predicted_activity WHERE station_id = ?1 ORDER BY day DESC, hour DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<PredictedActivity> getPredictedActivitiesByStationIdWithLimitAndOffset(UUID stationId, int limit, int offset);

    @Query(value = "SELECT u FROM PredictedActivity u ORDER BY day ASC")
    List<PredictedActivity> getPredictedActivitiesOrderedByDayAscending();
}
