package com.thesis.webapi.services.impl;

import com.thesis.webapi.entities.Bicycle;
import com.thesis.webapi.entities.Staff;
import com.thesis.webapi.repositories.BicycleRepository;
import com.thesis.webapi.repositories.StaffRepository;
import com.thesis.webapi.services.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class SimulationServiceImpl implements SimulationService {

    private BicycleRepository bicycleRepository;

    private StaffRepository staffRepository;

    @Autowired
    public SimulationServiceImpl(BicycleRepository bicycleRepository, StaffRepository staffRepository) {
        this.bicycleRepository = bicycleRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public void changeStatusForArrivedTransportBicycles() {
        Date date = new Date();
        LocalTime localTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        int hour = localTime.getHour();
        if(hour > 5 && hour < 22) {
            System.out.println("[Simulation] Checking for finished transports...");
            List<Bicycle> bicycleList = bicycleRepository.getArrivedTransportBicycles(localTime);
            for(Bicycle bicycle : bicycleList) {
                bicycle.setStatus("Station");
                bicycle.setArrivalTime(null);
                bicycleRepository.save(bicycle);
            }
            System.out.println("[Simulation] Changed status and arrival time for " + bicycleList.size() + " bicycle(s).");
        }
    }

    @Override
    @Scheduled(cron = "0 */5 * * * *")
    public void onScheduleCallChangeStatusForArrivedTransportBicycles() {
        changeStatusForArrivedTransportBicycles();
    }

    @Override
    public void changeAvailabilityForDrivers() {
        List<Staff> staffList = staffRepository.getUnavailableDriversForTransport();
        System.out.println("[Simulation] Changing availability status for " + staffList.size() + " driver(s)...");
        for(Staff staff : staffList) {
            staff.setAvailable(true);
            staffRepository.save(staff);
        }
    }

    @Override
    @Scheduled(cron = "0 30 * * * *")
    public void onScheduleCallChangeAvailabilityForDrivers() {
        changeAvailabilityForDrivers();
    }
}
