package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Activity;
import com.thesis.webapi.entities.Bicycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    List<Activity> getActivitiesByStationIdAndDay(UUID stationId, Date day);

    Activity getActivityByStationIdAndDayAndHourFrom(UUID stationId, Date day, int hourFrom);

    List<Activity> getActivitiesByStationId(UUID stationId);

    @Query(value = "SELECT * FROM Activity WHERE station_id = ?1 ORDER BY day DESC, hour_from DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<Activity> getActivitiesByStationIdWithLimitAndOffset(UUID stationId, int limit, int offset);

    @Query(value = "SELECT * FROM Activity WHERE station_id = ?1 AND hour_from = ?2 ORDER BY day DESC LIMIT ?3", nativeQuery = true)
    List<Activity> getActivitiesByStationIdAndHourWithLimit(UUID stationId, int hour, int limit);

    @Query(value = "SELECT u FROM Activity u ORDER BY day ASC")
    List<Activity> getActivitiesOrderedByDayAscending();

}
