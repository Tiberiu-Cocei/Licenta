package entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Activity {

    private UUID id;

    private UUID stationId;

    private Date day;

    private int hourFrom;

    private int hourTo;

    private int bicyclesTaken;

    private int bicyclesBrought;

    private int discountsFrom;

    private int discountsTo;

    private boolean wasStationEmpty;

    private boolean wasStationFull;

    private int timesClickedWhileEmpty;

    private int timesClickedWhileFull;

    public static List<Activity> createActivityListFromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<List<Activity>>(){});
    }

    public UUID getId() {
        return id;
    }

    public UUID getStationId() {
        return stationId;
    }

    public Date getDay() {
        return day;
    }

    public int getHourFrom() {
        return hourFrom;
    }

    public int getHourTo() {
        return hourTo;
    }

    public int getBicyclesTaken() {
        return bicyclesTaken;
    }

    public int getBicyclesBrought() {
        return bicyclesBrought;
    }

    public int getDiscountsFrom() {
        return discountsFrom;
    }

    public int getDiscountsTo() {
        return discountsTo;
    }

    public boolean isWasStationEmpty() {
        return wasStationEmpty;
    }

    public boolean isWasStationFull() {
        return wasStationFull;
    }

    public int getTimesClickedWhileEmpty() {
        return timesClickedWhileEmpty;
    }

    public int getTimesClickedWhileFull() {
        return timesClickedWhileFull;
    }
}
