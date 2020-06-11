package com.thesis.webapi.services.impl;

import com.thesis.webapi.entities.Activity;
import com.thesis.webapi.entities.City;
import com.thesis.webapi.entities.PredictedActivity;
import com.thesis.webapi.entities.Station;
import com.thesis.webapi.repositories.ActivityRepository;
import com.thesis.webapi.repositories.CityRepository;
import com.thesis.webapi.repositories.PredictedActivityRepository;
import com.thesis.webapi.repositories.StationRepository;
import com.thesis.webapi.services.PredictedActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PredictedActivityServiceImpl implements PredictedActivityService {

    private final PredictedActivityRepository predictedActivityRepository;

    private final ActivityRepository activityRepository;

    private final CityRepository cityRepository;

    private final StationRepository stationRepository;

    private final static int PAST_ACTIVITY_LIMIT_FOR_PREDICTION = 100;

    @Autowired
    public PredictedActivityServiceImpl(PredictedActivityRepository predictedActivityRepository,
                                        ActivityRepository activityRepository,
                                        CityRepository cityRepository,
                                        StationRepository stationRepository) {
        this.predictedActivityRepository = predictedActivityRepository;
        this.activityRepository = activityRepository;
        this.cityRepository = cityRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public ResponseEntity<List<PredictedActivity>> getPredictedActivitiesByStationIdWithLimitAndOffset(UUID stationId, int limit, int offset) {
        return new ResponseEntity<>(
                predictedActivityRepository.getPredictedActivitiesByStationIdWithLimitAndOffset(stationId, limit, offset), HttpStatus.OK);
    }

    @Override
    public void calculateAllPredictedActivitiesForToday() {
        Date date = new Date();
        System.out.println("Current date is " + date.toString() + ".");
        List<City> cityList = cityRepository.findAll();
        for(City city : cityList) {
            System.out.println("Checking stations for the city " + city.getName() + "...");
            List<Station> stationList = stationRepository.getStationsByCityId(city.getCityId());
            for(Station station : stationList) {
                List<PredictedActivity> pActivityList = predictedActivityRepository.getPredictedActivityByStationIdAndDay(station.getId(), date);
                if(pActivityList.size() != 0) {
                    System.out.println("Station " + station.getName() + " already has predicted activities generated.");
                }
                else {
                    System.out.println("Generating predicted activities for station " + station.getName() + "...");
                    int minVal = (int)( ((float)station.getMaxCapacity()) / 100 * 15);
                    int maxVal = (int)( ((float)station.getMaxCapacity()) / 100 * 85);
                    for(int hour = 7; hour <= 21; hour++) {
                        calculatePredictedActivityForStationAndHour(station.getId(), date, hour, minVal, maxVal);
                    }
                }
            }
        }
    }

    private void calculatePredictedActivityForStationAndHour(UUID stationId, Date day, int hour, int minVal, int maxVal) {
        List<Activity> pastActivities =
                activityRepository.getActivitiesByStationIdAndHourWithLimit(stationId, hour, PAST_ACTIVITY_LIMIT_FOR_PREDICTION);
        ArrayList<Integer> calculatedHourlyBicycleNr = new ArrayList<>();
        for(Activity activity : pastActivities) {
            int calculatedBicycleNr = 0;
            calculatedBicycleNr += activity.getBicyclesTaken();
            calculatedBicycleNr -= activity.getBicyclesBrought();
            calculatedBicycleNr += activity.getDiscountsTo();
            calculatedBicycleNr -= activity.getDiscountsFrom();
            calculatedBicycleNr += activity.getTimesClickedWhileEmpty() / 100;
            calculatedBicycleNr -= activity.getTimesClickedWhileFull() / 100;

            if(calculatedBicycleNr < minVal) {
                calculatedBicycleNr = minVal;
            }
            else if(calculatedBicycleNr > maxVal) {
                calculatedBicycleNr = maxVal;
            }

            calculatedHourlyBicycleNr.add(calculatedBicycleNr);
        }

        double predictedBicycleNr = 0.0d;
        double currentRate = 1;
        double rateSum = 0;
        for(int calculatedBicycleNr : calculatedHourlyBicycleNr) {
            predictedBicycleNr += (float) calculatedBicycleNr / currentRate;
            rateSum += currentRate;
            currentRate += 0.05;
        }

        if(rateSum > 0) {
            predictedBicycleNr /= rateSum;
            PredictedActivity predictedActivity = new PredictedActivity(stationId, day, hour, (int)predictedBicycleNr);
            predictedActivityRepository.save(predictedActivity);
        }
    }

    @Override
    @Scheduled(cron = "0 0 4 * * *")
    public void onScheduleCallCalculateAllPredictedActivitiesForToday() {
        calculateAllPredictedActivitiesForToday();
    }

    @Override
    @EventListener(ContextRefreshedEvent.class)
    public void onStartupCallCalculateAllPredictedActivitiesForToday() {
        calculateAllPredictedActivitiesForToday();
    }
}
