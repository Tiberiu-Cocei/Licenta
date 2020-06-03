package com.thesis.webapi.dtos;

public class BicycleStatusDto {

    private int stationNumber;

    private int warehouseNumber;

    private int transportNumber;

    private int userNumber;

    private int damagedNumber;

    private int stolenNumber;

    public BicycleStatusDto(int stationNumber, int warehouseNumber, int transportNumber, int userNumber, int damagedNumber, int stolenNumber) {
        this.stationNumber = stationNumber;
        this.warehouseNumber = warehouseNumber;
        this.transportNumber = transportNumber;
        this.userNumber = userNumber;
        this.damagedNumber = damagedNumber;
        this.stolenNumber = stolenNumber;
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(int stationNumber) {
        this.stationNumber = stationNumber;
    }

    public int getWarehouseNumber() {
        return warehouseNumber;
    }

    public void setWarehouseNumber(int warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    public int getTransportNumber() {
        return transportNumber;
    }

    public void setTransportNumber(int transportNumber) {
        this.transportNumber = transportNumber;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public int getDamagedNumber() {
        return damagedNumber;
    }

    public void setDamagedNumber(int damagedNumber) {
        this.damagedNumber = damagedNumber;
    }

    public int getStolenNumber() {
        return stolenNumber;
    }

    public void setStolenNumber(int stolenNumber) {
        this.stolenNumber = stolenNumber;
    }
}
