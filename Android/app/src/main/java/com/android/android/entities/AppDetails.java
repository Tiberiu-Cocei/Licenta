package com.android.android.entities;

import java.util.List;
import java.util.UUID;

public class AppDetails {

    private static AppDetails appDetails = null;

    private City city;

    private List<Station> stationList;

    private UUID stationId;

    private String stationCoordinates;

    private String stationName;

    private UUID bicycleId;

    private Transaction transaction;

    public static AppDetails getAppDetails() {
        if(appDetails == null) {
            appDetails = new AppDetails();
        }
        return appDetails;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Station> getStationList() {
        return stationList;
    }

    public void setStationList(List<Station> stationList) {
        this.stationList = stationList;
    }

    public UUID getStationId() {
        return stationId;
    }

    public void setStationId(UUID stationId) {
        this.stationId = stationId;
    }

    public UUID getBicycleId() {
        return bicycleId;
    }

    public void setBicycleId(UUID bicycleId) {
        this.bicycleId = bicycleId;
    }

    public String getStationCoordinates() {
        return stationCoordinates;
    }

    public void setStationCoordinates(String stationCoordinates) {
        this.stationCoordinates = stationCoordinates;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
