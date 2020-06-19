package com.thesis.webapi.services.impl;

import com.thesis.webapi.entities.*;
import com.thesis.webapi.enums.BicycleStatus;
import com.thesis.webapi.repositories.*;
import com.thesis.webapi.services.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class SimulationServiceImpl implements SimulationService {

    private final BicycleRepository bicycleRepository;

    private final StaffRepository staffRepository;

    private final StationRepository stationRepository;

    private final ActivityRepository activityRepository;

    private final CityRepository cityRepository;

    private final PredictedActivityRepository predictedActivityRepository;

    @Autowired
    public SimulationServiceImpl(BicycleRepository bicycleRepository,
                                 StaffRepository staffRepository,
                                 StationRepository stationRepository,
                                 ActivityRepository activityRepository,
                                 CityRepository cityRepository,
                                 PredictedActivityRepository predictedActivityRepository) {
        this.bicycleRepository = bicycleRepository;
        this.staffRepository = staffRepository;
        this.stationRepository = stationRepository;
        this.activityRepository = activityRepository;
        this.cityRepository = cityRepository;
        this.predictedActivityRepository = predictedActivityRepository;
    }

    @Override
    public void generateBicyclesForStations() {
        List<City> cityList = cityRepository.findAll();
        List<Character> randomChars = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k');
        Random random = new Random();
        for(City city : cityList) {
            List<Station> stationList = stationRepository.getStationsByCityId(city.getCityId());
            for(Station station : stationList) {
                int nrOfBicyclesToGenerate = station.getMaxCapacity() - station.getCurrentCapacity() - 5;
                nrOfBicyclesToGenerate -= random.nextInt((int)(station.getMaxCapacity() * 0.80));
                for(int i = 0; i < nrOfBicyclesToGenerate; i++) {
                    StringBuilder modelBuilder = new StringBuilder();
                    for(int j = 0; j < 3 ; j++) {
                        modelBuilder.append(randomChars.get(random.nextInt(randomChars.size())));
                    }
                    modelBuilder.append('-');
                    modelBuilder.append(random.nextInt(899) + 100);
                    String model = modelBuilder.toString();
                    Bicycle bicycle = new Bicycle(station.getId(), model);
                    bicycle.setStatus(BicycleStatus.STATION.getValue());
                    bicycleRepository.save(bicycle);
                    station.incrementCurrentCapacity();
                    stationRepository.save(station);
                }
            }
        }
    }

    @Override
    public void updateActivityNumbers() {
        HashMap<UUID, Station> idStationHashMap = new HashMap<>();
        List<Station> stationList = stationRepository.findAll();
        for(Station station : stationList) {
            idStationHashMap.put(station.getId(), station);
        }

        Random random = new Random();
        List<Activity> activityList = activityRepository.findAll();
        for(Activity activity : activityList) {
            Station station = idStationHashMap.get(activity.getStationId());
            int bicyclesTaken = Math.max(random.nextInt((int)(station.getMaxCapacity() * 2.0)), 5);
            boolean moreBicyclesTakenThanBrought = random.nextBoolean();
            int bicyclesBrought;
            if(moreBicyclesTakenThanBrought) {
                bicyclesBrought = Math.max(bicyclesTaken - random.nextInt((int)(bicyclesTaken * 0.5)), 5);
            }
            else {
                bicyclesBrought = Math.max(bicyclesTaken + random.nextInt((int)(bicyclesTaken * 0.5)), 5);
            }
            int discountsFrom = random.nextInt((int)(bicyclesTaken * 0.25));
            int discountsTo = random.nextInt((int)(bicyclesBrought * 0.25));

            List<Boolean> randomBooleans = Arrays.asList(false, false, false, true, false);
            boolean wasStationFull = randomBooleans.get(random.nextInt(randomBooleans.size()));
            boolean wasStationEmpty = randomBooleans.get(random.nextInt(randomBooleans.size()));
            int timesClickedWhileFull = 0;
            int timesClickedWhileEmpty = 0;

            if(wasStationFull) {
                 timesClickedWhileFull = random.nextInt((int)(bicyclesBrought * 2.5));
            }

            if(wasStationEmpty) {
                timesClickedWhileEmpty = random.nextInt((int)(bicyclesTaken * 2.5));
            }

            activity.setBicyclesTaken(bicyclesTaken);
            activity.setBicyclesBrought(bicyclesBrought);
            activity.setDiscountsFrom(discountsFrom);
            activity.setDiscountsTo(discountsTo);
            activity.setWasStationFull(wasStationFull);
            activity.setWasStationEmpty(wasStationEmpty);
            activity.setTimesClickedWhileFull(timesClickedWhileFull);
            activity.setTimesClickedWhileEmpty(timesClickedWhileEmpty);
            activityRepository.save(activity);
        }
    }

    @Override
    public void generateDependentVariablesForLinearRegression() {
        List<Station> stationList = stationRepository.findAll();
        HashMap<UUID, Integer> minValues = new HashMap<>();
        HashMap<UUID, Integer> maxValues = new HashMap<>();
        for(Station station : stationList) {
            int minVal = (int)( ((float)station.getMaxCapacity()) / 100 * 5);
            int maxVal = (int)( ((float)station.getMaxCapacity()) / 100 * 95);
            minValues.put(station.getId(), minVal);
            maxValues.put(station.getId(), maxVal);
        }
        List<Activity> activityList = activityRepository.getActivitiesOrderedByDayAscending();
        Date date = new Date();
        for(Activity activity : activityList) {
            if(activity.getDay().compareTo(date) >= 0) {
                break;
            }
            int calculatedBicycleNr = 0;
            calculatedBicycleNr += activity.getBicyclesTaken();
            calculatedBicycleNr -= activity.getBicyclesBrought() * 0.5;
            calculatedBicycleNr += activity.getDiscountsTo() * 0.5;
            calculatedBicycleNr -= activity.getDiscountsFrom() * 0.5;
            calculatedBicycleNr += activity.getTimesClickedWhileEmpty() * 0.0025;
            calculatedBicycleNr -= activity.getTimesClickedWhileFull() * 0.0025;

            int minVal = minValues.get(activity.getStationId());
            int maxVal = maxValues.get(activity.getStationId());

            if(calculatedBicycleNr < minVal) {
                calculatedBicycleNr = minVal;
            }
            else if(calculatedBicycleNr > maxVal) {
                calculatedBicycleNr = maxVal;
            }

            PredictedActivity predictedActivity = new PredictedActivity(
                    activity.getStationId(), activity.getDay(), activity.getHourFrom(), calculatedBicycleNr);
            predictedActivityRepository.save(predictedActivity);
        }
    }

    @Override
    public void changeStatusForArrivedTransportBicycles() {
        Date date = new Date();
        LocalTime localTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        int hour = localTime.getHour();
        if(hour > 5 && hour < 22) {
            System.out.println("[Simulation] Checking for finished transports...");
            List<Bicycle> bicycleList = bicycleRepository.getArrivedTransportBicycles(localTime);
            for(Bicycle bicycle : bicycleList) {
                bicycle.setStatus(BicycleStatus.STATION.getValue());
                bicycle.setArrivalTime(null);
                bicycleRepository.save(bicycle);
            }
            System.out.println("[Simulation] Changed status and arrival time for " + bicycleList.size() + " bicycle(s).");
        }
    }

    @Override
    @Scheduled(cron = "0 */5 * * * *")
    public void onScheduleCallChangeStatusForArrivedTransportBicycles() {
        changeStatusForArrivedTransportBicycles();
    }

    @Override
    public void changeAvailabilityForDrivers() {
        List<Staff> staffList = staffRepository.getUnavailableDriversForTransport();
        System.out.println("[Simulation] Changing availability status for " + staffList.size() + " driver(s)...");
        for(Staff staff : staffList) {
            staff.setAvailable(true);
            staffRepository.save(staff);
        }
    }

    @Override
    @Scheduled(cron = "0 30 * * * *")
    public void onScheduleCallChangeAvailabilityForDrivers() {
        changeAvailabilityForDrivers();
    }

//    @EventListener(ContextRefreshedEvent.class)
//    public void onStartupCallCalculateAllPredictedActivitiesForToday() {
//        //generateBicyclesForStations();
//        //updateActivityNumbers();
//        generateDependentVariablesForLinearRegression();
//    }
}
