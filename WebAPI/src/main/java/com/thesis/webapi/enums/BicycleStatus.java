package com.thesis.webapi.enums;

public enum BicycleStatus {

    STATION("Station"),
    WAREHOUSE("Warehouse"),
    TRANSPORT("Transport"),
    USER("User"),
    DAMAGED("Damaged"),
    STOLEN("Stolen");

    private final String status;

    BicycleStatus(final String status) {
        this.status = status;
    }

    public String getValue() {
        return this.status;
    }

}
