package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    List<Activity> getActivitiesByStationIdAndDay(UUID stationId, Date day);

    Activity getActivityByStationIdAndDayAndHourFrom(UUID stationId, Date day, int hourFrom);

    List<Activity> getActivitiesByStationId(UUID stationId);

}
