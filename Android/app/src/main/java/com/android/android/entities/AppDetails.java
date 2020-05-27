package com.android.android.entities;

import java.util.List;
import java.util.UUID;

public class AppDetails {

    private static AppDetails appDetails = null;

    private City city;

    private List<Station> stationList;

    private UUID startStationId;

    private UUID plannedStationId;

    private String stationCoordinates;

    private String startStationName;

    private String plannedStationName;

    private boolean choosingPlannedStation;

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

    public UUID getStartStationId() {
        return startStationId;
    }

    public void setStartStationId(UUID startStationId) {
        this.startStationId = startStationId;
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

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getPlannedStationName() {
        return plannedStationName;
    }

    public void setPlannedStationName(String plannedStationName) {
        this.plannedStationName = plannedStationName;
    }

    public boolean isChoosingPlannedStation() {
        return choosingPlannedStation;
    }

    public void setChoosingPlannedStation(boolean choosingPlannedStation) {
        this.choosingPlannedStation = choosingPlannedStation;
    }

    public UUID getPlannedStationId() {
        return plannedStationId;
    }

    public void setPlannedStationId(UUID plannedStationId) {
        this.plannedStationId = plannedStationId;
    }
}
