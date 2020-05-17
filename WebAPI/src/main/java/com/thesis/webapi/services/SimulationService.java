package com.thesis.webapi.services;

public interface SimulationService {

    void changeStatusForArrivedTransportBicycles();

    void onScheduleCallChangeStatusForArrivedTransportBicycles();

    void changeAvailabilityForDrivers();

    void onScheduleCallChangeAvailabilityForDrivers();

}
