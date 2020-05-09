package com.thesis.webapi.services.impl;

import com.thesis.webapi.entities.*;
import com.thesis.webapi.repositories.*;
import com.thesis.webapi.services.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class TransportServiceImpl implements TransportService {

    private final TransportRepository transportRepository;

    private final TransportLineRepository transportLineRepository;

    private final BicycleRepository bicycleRepository;

    private final CityRepository cityRepository;

    private final StaffRepository staffRepository;

    private final StationRepository stationRepository;

    private final Random random;

    @Autowired
    public TransportServiceImpl(TransportRepository transportRepository,
                                TransportLineRepository transportLineRepository,
                                BicycleRepository bicycleRepository,
                                CityRepository cityRepository,
                                StaffRepository staffRepository,
                                StationRepository stationRepository) {
        this.transportRepository = transportRepository;
        this.transportLineRepository = transportLineRepository;
        this.bicycleRepository = bicycleRepository;
        this.cityRepository = cityRepository;
        this.staffRepository = staffRepository;
        this.stationRepository = stationRepository;
        this.random = new Random();
    }

    @Override
    public void transportDamagedBicyclesToWarehouse() {
        Date date = new Date();
        int hour = date.toInstant().atZone(ZoneId.systemDefault()).getHour();
        if(hour > 5 && hour < 20) {
            System.out.println("Checking for damaged bicycles...");
            List<City> cityList = cityRepository.findAll();
            for(City city : cityList) {
                List<Bicycle> bicycleList = bicycleRepository.getDamagedBicycles(city.getCityId());
                if(bicycleList.size() != 0) {
                    System.out.println("The city " + city.getName() + " has " + bicycleList.size() + " damaged bicycle(s).");
                }
                if(bicycleList.size() > 5) {
                    List<Staff> staffList = staffRepository.getAvailableDriversForTransport();
                    if(staffList.size() > 0) {
                        System.out.println("Creating transport for the damaged bicycles...");
                        int randomIndex = random.nextInt(staffList.size());
                        Staff staff = staffList.get(randomIndex);
                        Transport transport = new Transport(staff.getId(), date);
                        transportRepository.save(transport);
                        UUID warehouseId = stationRepository.getWarehouseId(city.getCityId());
                        Date arrivalTime = new Date(date.getTime() + 120 * 60);
                        for(Bicycle bicycle : bicycleList) {
                            TransportLine transportLine = new TransportLine(transport.getId(), bicycle.getId(),
                                    bicycle.getStationId(), warehouseId, arrivalTime);
                            transportLineRepository.save(transportLine);
                        }
                    }
                    else {
                        System.out.println("Cannot create transport for the damaged bicycles. There is no available driver.");
                    }
                }
            }
        }
    }

    @Override
    @Scheduled(cron = "0 0 */3 * * *")
    public void onScheduleCallTransportDamagedBicyclesToWarehouse() {
        transportDamagedBicyclesToWarehouse();
    }

    @Override
    @EventListener(ContextRefreshedEvent.class)
    public void onStartupCallTransportDamagedBicyclesToWarehouse() {
        transportDamagedBicyclesToWarehouse();
    }
}
