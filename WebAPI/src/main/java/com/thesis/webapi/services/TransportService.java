package com.thesis.webapi.services;

public interface TransportService {

    void transportDamagedBicyclesToWarehouse();

    void onScheduleCallTransportDamagedBicyclesToWarehouse();

    void onStartupCallTransportDamagedBicyclesToWarehouse();

}
