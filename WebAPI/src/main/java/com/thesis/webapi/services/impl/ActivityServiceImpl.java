package com.thesis.webapi.services.impl;

import com.thesis.webapi.entities.Activity;
import com.thesis.webapi.entities.City;
import com.thesis.webapi.entities.Station;
import com.thesis.webapi.repositories.ActivityRepository;
import com.thesis.webapi.repositories.CityRepository;
import com.thesis.webapi.repositories.StationRepository;
import com.thesis.webapi.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;

    private final CityRepository cityRepository;

    private final StationRepository stationRepository;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository,
                               CityRepository cityRepository,
                               StationRepository stationRepository) {
        this.activityRepository = activityRepository;
        this.cityRepository = cityRepository;
        this.stationRepository = stationRepository;
    }

    private Activity getActivityForIncrement(UUID stationId) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        int hourFrom = calendar.get(Calendar.HOUR_OF_DAY);
        return activityRepository.getActivityByStationIdAndDayAndHourFrom(stationId, date, hourFrom);
    }

    @Override
    public void incrementTimesClickedWhileEmpty(UUID stationId) {
        Activity activity = getActivityForIncrement(stationId);
        activity.incrementTimesClickedWhileEmpty();
        activityRepository.save(activity);
    }

    @Override
    public void incrementTimesClickedWhileFull(UUID stationId) {
        Activity activity = getActivityForIncrement(stationId);
        activity.incrementTimesClickedWhileFull();
        activityRepository.save(activity);
    }

    @Override
    public void generateRowsForActivity() {
        Date date = new Date();
        System.out.println("Current date is " + date.toString() + ".");
        List<City> cityList = cityRepository.findAll();
        for(City city : cityList) {
            System.out.println("Checking stations for the city " + city.getName() + "...");
            List<Station> stationList = stationRepository.getStationsByCityId(city.getCityId());
            for(Station station : stationList) {
                List<Activity> activityList = activityRepository.getActivitiesByStationIdAndDay(station.getId(), date);
                if(activityList.size() != 0) {
                    System.out.println("Station " + station.getName() + " already has activity rows generated.");
                }
                else {
                    System.out.println("Generating activity rows for station " + station.getName() + "...");
                    for(int hour = 7; hour <= 21; hour++) {
                        Activity activity = new Activity(station.getId(), date, hour);
                        activityRepository.save(activity);
                    }
                }
            }
        }
    }

    @Override
    @Scheduled(cron = "0 0 3 * * *")
    public void onScheduleCallGenerateRowsForActivity() {
        generateRowsForActivity();
    }

    @Override
    @EventListener(ContextRefreshedEvent.class)
    public void onStartupCallGenerateRowsForActivity() {
        generateRowsForActivity();
    }
}
