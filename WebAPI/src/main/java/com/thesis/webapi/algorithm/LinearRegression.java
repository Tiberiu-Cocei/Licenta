package com.thesis.webapi.algorithm;

import com.thesis.webapi.entities.Activity;
import com.thesis.webapi.entities.PredictedActivity;
import com.thesis.webapi.entities.Station;
import com.thesis.webapi.repositories.ActivityRepository;
import com.thesis.webapi.repositories.PredictedActivityRepository;
import com.thesis.webapi.repositories.StationRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LinearRegression {

    private BigDecimal sumX;

    private BigDecimal sumSquaredX;

    private BigDecimal sumY;

    private BigDecimal sumXY;

    private HashMap<UUID, Integer> minValues;

    private HashMap<UUID, Integer> maxValues;

    private int n;

    private final ActivityRepository activityRepository;

    private final PredictedActivityRepository predictedActivityRepository;

    private final StationRepository stationRepository;

    public LinearRegression(ActivityRepository activityRepository,
                            PredictedActivityRepository predictedActivityRepository,
                            StationRepository stationRepository) {
        this.activityRepository = activityRepository;
        this.predictedActivityRepository = predictedActivityRepository;
        this.stationRepository = stationRepository;
    }

    public double getPredictedNumber(Activity activity) {
        init();
        calculateSums();
        return solveEquation(getX(activity));
    }

    private void init() {
        sumX = new BigDecimal("0");
        sumSquaredX = new BigDecimal("0");
        sumY = new BigDecimal("0");
        sumXY = new BigDecimal("0");

        List<Station> stationList = stationRepository.findAll();
        minValues = new HashMap<>();
        maxValues = new HashMap<>();
        for(Station station : stationList) {
            int minVal = (int)( ((float)station.getMaxCapacity()) / 100 * 5);
            int maxVal = (int)( ((float)station.getMaxCapacity()) / 100 * 95);
            minValues.put(station.getId(), minVal);
            maxValues.put(station.getId(), maxVal);
        }
    }

    private void calculateSums() {
        List<Activity> activityList = activityRepository.getActivitiesOrderedByDayAscending();
        List<PredictedActivity> predictedActivityList = predictedActivityRepository.getPredictedActivitiesOrderedByDayAscending();
        while(activityList.size() > 0 && predictedActivityList.size() > 0) {
            Activity activity = activityList.get(0);
            activityList.remove(0);

            PredictedActivity predictedActivity = predictedActivityList.get(0);
            predictedActivityList.remove(0);

            if(activity.getDay().after(predictedActivity.getDay())) {
                while(activity.getDay().compareTo(predictedActivity.getDay()) != 0) {
                    if(predictedActivityList.size() > 0) {
                        predictedActivity = predictedActivityList.get(0);
                        predictedActivityList.remove(0);
                    }
                    else {
                        return;
                    }
                }
            }
            else if(activity.getDay().before(predictedActivity.getDay())) {
                while(activity.getDay().compareTo(predictedActivity.getDay()) != 0) {
                    if(activityList.size() > 0) {
                        activity = activityList.get(0);
                        activityList.remove(0);
                    }
                    else {
                        return;
                    }
                }
            }

            n++;

            double X = getX(activity);
            double Y = getY(predictedActivity);

            sumX = sumX.add(BigDecimal.valueOf(X));
            sumSquaredX = sumSquaredX.add(BigDecimal.valueOf(X * X));
            sumY = sumY.add(BigDecimal.valueOf(Y));
            sumXY = sumXY.add(BigDecimal.valueOf(X * Y));
        }
    }

    private double solveEquation(double x) {
        BigDecimal b1 = sumXY.multiply(BigDecimal.valueOf(n)).subtract(sumX.multiply(sumY));
        BigDecimal b2 = sumSquaredX.multiply(BigDecimal.valueOf(n)).subtract(sumX.multiply(sumX));
        BigDecimal b = b1.divide(b2, 2, RoundingMode.HALF_UP);

        BigDecimal a = sumY.subtract(sumX.multiply(b)).divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP);

        double aAsDouble = a.doubleValue();
        double bAsDouble = b.doubleValue();

        return aAsDouble + bAsDouble * x;
    }

    private double getX(Activity activity) {
        double calculatedBicycleNr = 0.0d;
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

        return calculatedBicycleNr;
    }

    private double getY(PredictedActivity predictedActivity) {
        return predictedActivity.getNumberOfBicycles();
    }
}
