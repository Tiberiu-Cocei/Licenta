package com.android.android.utilities;

import com.android.android.activities.MapActivity;

public class DistanceCalculator {

    public static class Coordinates {

        private Double latitude;

        private Double longitude;

        Coordinates(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }

    public static Coordinates getCoordinatesFromString(String coordinates) {
        coordinates = coordinates.substring(1, coordinates.length()-1);
        String[] coordinatesSplit = coordinates.split(",");
        Double latitude = Double.valueOf(coordinatesSplit[0]);
        Double longitude = Double.valueOf(coordinatesSplit[1]);
        return new Coordinates(latitude, longitude);
    }

    public static boolean isCloseToStation(String coordinates) {
        if(coordinates == null) {
            return false;
        }

        Coordinates givenCoordinates = getCoordinatesFromString(coordinates);
        Double currentLatitude = MapActivity.getLatitude();
        Double currentLongitude = MapActivity.getLongitude();

        if(currentLatitude != null && currentLongitude != null) {
            return Math.abs(givenCoordinates.latitude - currentLatitude) < 0.00225 &&
                    Math.abs(givenCoordinates.longitude - currentLongitude) < 0.0025;
        }

        return false;
    }

}
