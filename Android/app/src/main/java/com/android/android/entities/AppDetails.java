package com.android.android.entities;

import java.util.List;
import java.util.UUID;

public class AppDetails {

    private static AppDetails appDetails = null;

    private City city;

    private List<Station> stationList;

    private Station startStation;

    private UUID plannedStationId;

    private String plannedStationName;

    private boolean choosingPlannedStation;

    private UUID bicycleId;

    private Transaction transaction;

    private Discount discount;

    public static AppDetails getAppDetails() {
        if(appDetails == null) {
            appDetails = new AppDetails();
        }
        return appDetails;
    }

    public static void resetTransactionValues() {
        if(appDetails != null) {
            appDetails.setChoosingPlannedStation(false);
            appDetails.setPlannedStationName(null);
            appDetails.setPlannedStationId(null);
            appDetails.setDiscount(null);
        }
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

    public UUID getBicycleId() {
        return bicycleId;
    }

    public void setBicycleId(UUID bicycleId) {
        this.bicycleId = bicycleId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
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

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Station getStartStation() {
        return startStation;
    }

    public void setStartStation(Station startStation) {
        this.startStation = startStation;
    }
}
