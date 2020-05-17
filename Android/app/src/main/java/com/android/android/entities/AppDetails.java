package com.android.android.entities;

import java.util.List;

public class AppDetails {

    private static AppDetails appDetails = null;

    private City city;

    private List<Station> stationList;

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
}
