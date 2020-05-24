package com.android.android.utilities;

import com.android.android.activities.MapActivity;

public class DistanceCalculator {

    public static boolean isCloseToStation(String coordinates) {
        if(coordinates == null) {
            return false;
        }

        coordinates = coordinates.substring(1, coordinates.length()-1);
        String[] coordinatesSplit = coordinates.split(",");
        Double givenLatitude = Double.valueOf(coordinatesSplit[0]);
        Double givenLongitude = Double.valueOf(coordinatesSplit[1]);
        Double currentLatitude = MapActivity.getLatitude();
        Double currentLongitude = MapActivity.getLongitude();

        if(currentLatitude != null && currentLongitude != null) {
            return Math.abs(givenLatitude - currentLatitude) < 0.00225 &&
                    Math.abs(givenLongitude - currentLongitude) < 0.0025;
        }

        return false;
    }

}
