package com.android.android.entities;

public class AppDetails {

    private static AppDetails appDetails = null;

    private City city;

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
}
