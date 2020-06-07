package com.thesis.webapi.services.impl;

import com.thesis.webapi.dtos.BicycleStatusDto;
import com.thesis.webapi.entities.AppTransaction;
import com.thesis.webapi.entities.Bicycle;
import com.thesis.webapi.entities.Station;
import com.thesis.webapi.enums.BicycleStatus;
import com.thesis.webapi.repositories.AppTransactionRepository;
import com.thesis.webapi.repositories.BicycleRepository;
import com.thesis.webapi.repositories.StationRepository;
import com.thesis.webapi.services.BicycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BicycleServiceImpl implements BicycleService {

    private final BicycleRepository bicycleRepository;

    private final StationRepository stationRepository;

    private final AppTransactionRepository appTransactionRepository;

    @Autowired
    public BicycleServiceImpl(BicycleRepository bicycleRepository,
                              StationRepository stationRepository,
                              AppTransactionRepository appTransactionRepository) {
        this.bicycleRepository = bicycleRepository;
        this.stationRepository = stationRepository;
        this.appTransactionRepository = appTransactionRepository;
    }

    @Override
    public ResponseEntity<List<Bicycle>> getBicyclesByStationId(UUID stationId) {
        return new ResponseEntity<>(bicycleRepository.getBicyclesByStationId(stationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Bicycle> getBicycleById(UUID bicycleId) {
        Bicycle bicycle = bicycleRepository.getBicycleById(bicycleId);
        if(bicycle != null) {
            return new ResponseEntity<>(bicycle, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<BicycleStatusDto> getBicycleCountByStatus(UUID stationId) {
        int stationNumber, warehouseNumber, transportNumber, userNumber, damagedNumber, stolenNumber;
        if(stationId == null) {
            stationNumber = bicycleRepository.getBicycleCountByStatus(BicycleStatus.STATION.getValue());
            warehouseNumber = bicycleRepository.getBicycleCountByStatus(BicycleStatus.WAREHOUSE.getValue());
            transportNumber = bicycleRepository.getBicycleCountByStatus(BicycleStatus.TRANSPORT.getValue());
            userNumber = bicycleRepository.getBicycleCountByStatus(BicycleStatus.USER.getValue());
            damagedNumber = bicycleRepository.getBicycleCountByStatus(BicycleStatus.DAMAGED.getValue());
            stolenNumber = bicycleRepository.getBicycleCountByStatus(BicycleStatus.STOLEN.getValue());
        }
        else {
            stationNumber = bicycleRepository.getBicycleCountByStatusAndStationId(BicycleStatus.STATION.getValue(), stationId);
            warehouseNumber = bicycleRepository.getBicycleCountByStatusAndStationId(BicycleStatus.WAREHOUSE.getValue(), stationId);
            transportNumber = bicycleRepository.getBicycleCountByStatusAndStationId(BicycleStatus.TRANSPORT.getValue(), stationId);
            userNumber = bicycleRepository.getBicycleCountByStatusAndStationId(BicycleStatus.USER.getValue(), stationId);
            damagedNumber = bicycleRepository.getBicycleCountByStatusAndStationId(BicycleStatus.DAMAGED.getValue(), stationId);
            stolenNumber = bicycleRepository.getBicycleCountByStatusAndStationId(BicycleStatus.STOLEN.getValue(), stationId);
        }

        BicycleStatusDto bicycleStatusDto = new BicycleStatusDto(
                stationNumber, warehouseNumber, transportNumber, userNumber, damagedNumber, stolenNumber);
        return new ResponseEntity<>(bicycleStatusDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Bicycle>> getBicyclesWithLimitAndOffset(Integer limit, Integer offset, UUID stationId) {
        if(stationId == null) {
            return new ResponseEntity<>(bicycleRepository.getBicyclesWithLimitAndOffset(limit, offset), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(
                    bicycleRepository.getBicyclesWithLimitAndOffsetAndStationId(stationId, limit, offset), HttpStatus.OK);
        }
    }

    @Override
    public void clearStationsOfLateBicycles() {
        Date date = new Date();
        LocalTime localTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        int hour = localTime.getHour();
        if(hour >= 7 && hour <= 22) {
            System.out.println("Checking for late users...");
            List<Bicycle> bicycleList = bicycleRepository.getLateBicycles(localTime);
            int numberOfChanges = bicycleList.size();
            for(Bicycle bicycle : bicycleList) {
                AppTransaction appTransaction = appTransactionRepository.getUnfinishedTransactionByBicycleId(bicycle.getId());
                if(appTransaction.getPlannedStationId() != null) {
                    bicycle.setStationId(null);
                    bicycleRepository.save(bicycle);
                    appTransaction.setPlannedStationId(null);
                    appTransactionRepository.save(appTransaction);
                }
                else {
                    numberOfChanges--;
                }
            }
            System.out.println("Modified station, transaction and bicycle for " + numberOfChanges + " cases.");
        }
    }

    @Override
    @Scheduled(cron = "30 */2 * * * *")
    public void onScheduleCallClearStationsOfLateBicycles() {
        clearStationsOfLateBicycles();
    }

}
