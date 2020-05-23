package com.android.android.utilities;

import com.android.android.activities.MapActivity;

public class DistanceCalculator {

    public static Double calculateDistance(String coordinates) {
        if(coordinates == null) {
            return null;
        }
        coordinates = coordinates.substring(1, coordinates.length()-1);
        String[] coordinatesSplit = coordinates.split(",");
        Double givenLatitude = Double.valueOf(coordinatesSplit[0]);
        Double givenLongitude = Double.valueOf(coordinatesSplit[1]);
        Double currentLatitude = MapActivity.getLatitude();
        Double currentLongitude = MapActivity.getLongitude();
        if(currentLatitude != null && currentLongitude != null) {
            double latitudeDifference = Math.abs(givenLatitude - currentLatitude);
            double longitudeDifference = Math.abs(givenLongitude - currentLongitude);
            return latitudeDifference + longitudeDifference;
        }
        return null;
    }

}
