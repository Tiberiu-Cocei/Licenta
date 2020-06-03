package com.thesis.webapi.services.impl;

import com.thesis.webapi.dtos.AppTransactionCreateDto;
import com.thesis.webapi.dtos.AppTransactionHistoryDto;
import com.thesis.webapi.dtos.AppTransactionPreviewDto;
import com.thesis.webapi.dtos.AppTransactionUpdateDto;
import com.thesis.webapi.entities.*;
import com.thesis.webapi.repositories.*;
import com.thesis.webapi.services.AppTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AppTransactionServiceImpl implements AppTransactionService {

    private final AppTransactionRepository appTransactionRepository;

    private final SettingsRepository settingsRepository;

    private final StationRepository stationRepository;

    private final DiscountRepository discountRepository;

    private final BicycleRepository bicycleRepository;

    private final AppUserRepository appUserRepository;

    @Autowired
    public AppTransactionServiceImpl(AppTransactionRepository appTransactionRepository,
                                     SettingsRepository settingsRepository,
                                     StationRepository stationRepository,
                                     DiscountRepository discountRepository,
                                     BicycleRepository bicycleRepository,
                                     AppUserRepository appUserRepository) {
        this.appTransactionRepository = appTransactionRepository;
        this.settingsRepository = settingsRepository;
        this.stationRepository = stationRepository;
        this.discountRepository = discountRepository;
        this.bicycleRepository = bicycleRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public ResponseEntity<List<AppTransactionHistoryDto>> getAppTransactionsByUserId(UUID userId) {
        List<AppTransaction> appTransactionList = appTransactionRepository.getAppTransactionsByUserId(userId);
        List<AppTransactionHistoryDto> userTransactions = convertAppTransactionsToHistoryDtos(appTransactionList);
        return new ResponseEntity<>(userTransactions, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AppTransactionHistoryDto>> getActiveAppTransactionByUserId(UUID userId) {
        List<AppTransaction> appTransactionList = appTransactionRepository.getUnfinishedTransactionsByUser(userId);
        List<AppTransactionHistoryDto> userTransactions = convertAppTransactionsToHistoryDtos(appTransactionList);
        return new ResponseEntity<>(userTransactions, HttpStatus.OK);
    }

    private List<AppTransactionHistoryDto> convertAppTransactionsToHistoryDtos(List<AppTransaction> appTransactionList) {
        List<AppTransactionHistoryDto> userTransactions = new ArrayList<>();
        for(AppTransaction appTransaction : appTransactionList) {
            String startStation = stationRepository.getStationNameById(appTransaction.getStartStationId());
            String plannedStation = null;
            String finishStation = null;
            if(appTransaction.getPlannedStationId() != null) {
                plannedStation = stationRepository.getStationNameById(appTransaction.getPlannedStationId());
            }
            if(appTransaction.getFinishStationId() != null) {
                finishStation = stationRepository.getStationNameById(appTransaction.getFinishStationId());
            }
            Double discountValue = null;
            if(appTransaction.getDiscountId() != null) {
                discountValue = discountRepository.getDiscountValueById(appTransaction.getDiscountId());
            }

            AppTransactionHistoryDto appTransactionHistoryDto = new AppTransactionHistoryDto.Builder()
                    .withStartStation(startStation)
                    .withPlannedStation(plannedStation)
                    .withFinishStation(finishStation)
                    .withDiscountValue(discountValue)
                    .withStartTime(appTransaction.getStartTime())
                    .withPlannedTime(appTransaction.getPlannedTime())
                    .withFinishTime(appTransaction.getFinishTime())
                    .withInitialCost(appTransaction.getInitialCost())
                    .withPenalty(appTransaction.getPenalty())
                    .build();

            userTransactions.add(appTransactionHistoryDto);
        }
        return userTransactions;
    }

    @Override
    public ResponseEntity<String> createTransaction(AppTransactionCreateDto appTransactionCreateDto) {
        AppUser appUser = appUserRepository.getAppUserById(appTransactionCreateDto.getUserId());
        if(appUser.isBanned()) {
            return new ResponseEntity<>("User is banned.", HttpStatus.BAD_REQUEST);
        }
        Settings settings = settingsRepository.getSettingsByCityId(appTransactionCreateDto.getCityId());
        if(settings == null) {
            return new ResponseEntity<>("No city settings found.", HttpStatus.BAD_REQUEST);
        }
        Bicycle bicycle = bicycleRepository.getBicycleById(appTransactionCreateDto.getBicycleId());
        if(bicycle == null) {
            return new ResponseEntity<>("No bicycle found for given id.", HttpStatus.BAD_REQUEST);
        }
        if(bicycle.getStatus().compareTo("Station") != 0) {
            return new ResponseEntity<>("Bicycle is not available for transport.", HttpStatus.BAD_REQUEST);
        }
        AppTransaction appTransaction = new AppTransaction(appTransactionCreateDto);
        if(appTransaction.getStartTime().compareTo(appTransaction.getPlannedTime()) > 0) {
            return new ResponseEntity<>("Planned finish time cannot be in the past.", HttpStatus.BAD_REQUEST);
        }
        Station startStation = stationRepository.getStationById(appTransaction.getStartStationId());
        if(startStation.getCurrentCapacity() <= 0) {
            return new ResponseEntity<>("There are no available bicycles in the start station.", HttpStatus.BAD_REQUEST);
        }
        Station plannedStation = stationRepository.getStationById(appTransaction.getPlannedStationId());
        if(plannedStation.getCurrentCapacity() >= plannedStation.getMaxCapacity()) {
            return new ResponseEntity<>("Planned station is at maximum capacity.", HttpStatus.BAD_REQUEST);
        }
        double dateDifference = appTransaction.getPlannedTime().getTime() - appTransaction.getStartTime().getTime();
        double minuteDifference = dateDifference / (60 * 1000);
        double initialCost = settings.getBasePrice() + Math.ceil(minuteDifference / settings.getIntervalTime()) * settings.getIntervalPrice();
        appTransaction.setInitialCost(initialCost);
        appTransactionRepository.save(appTransaction);
        return new ResponseEntity<>("Successfully created the transaction.", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> finalizeTransaction(AppTransactionUpdateDto appTransactionUpdateDto) {
        List<AppTransaction> unfinishedTransactions = appTransactionRepository.getUnfinishedTransactionsByUser(appTransactionUpdateDto.getUserId());
        if(unfinishedTransactions.size() == 0) {
            return new ResponseEntity<>("There are no transactions to finalize.", HttpStatus.BAD_REQUEST);
        }
        else if(unfinishedTransactions.size() > 1) {
            String message = "There is more than a transaction to finalize. Please contact an administrator.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        Station finishStation = stationRepository.getStationById(appTransactionUpdateDto.getFinishStationId());
        if(finishStation.getCurrentCapacity() >= finishStation.getMaxCapacity()) {
            return new ResponseEntity<>("Cannot finalize transaction. Station is at full capacity.", HttpStatus.BAD_REQUEST);
        }
        AppTransaction appTransaction = unfinishedTransactions.get(0);
        Date finishTime = new Date();
        if(appTransaction.getStartTime().compareTo(finishTime) > 0) {
            return new ResponseEntity<>("Finish time cannot be in the past.", HttpStatus.BAD_REQUEST);
        }
        appTransaction.setFinishStationId(appTransactionUpdateDto.getFinishStationId());
        appTransaction.setFinishTime(finishTime);
        if(appTransaction.getPlannedStationId() == null) {
            finishStation.incrementCurrentCapacity();
            stationRepository.save(finishStation);
        }
        appTransactionRepository.save(appTransaction);
        return new ResponseEntity<>("Successfully finished transaction", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Double> previewTransaction(AppTransactionPreviewDto appTransactionPreviewDto) {
        Settings settings = settingsRepository.getSettingsByCityId(appTransactionPreviewDto.getCityId());
        if(settings == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Discount discount = discountRepository.getDiscountById(appTransactionPreviewDto.getDiscountId());
        Date startTime = new Date();
        if(appTransactionPreviewDto.getPlannedTime().compareTo(startTime) < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        double dateDifference = appTransactionPreviewDto.getPlannedTime().getTime() - startTime.getTime();
        double minuteDifference = dateDifference / (60 * 1000);
        double initialCost = settings.getBasePrice() + Math.ceil(minuteDifference / settings.getIntervalTime()) * settings.getIntervalPrice();
        if(discount != null) {
            initialCost -= (discount.getDiscountValue() * initialCost) / 100;
        }
        return new ResponseEntity<>(initialCost, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AppTransaction>> getTransactionsByStartStationWithLimitAndOffset(UUID startStationId, Integer limit, Integer offset) {
        return new ResponseEntity<>(
                appTransactionRepository.getTransactionsByStartStationWithLimitAndOffset(startStationId, limit, offset), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AppTransaction>> getTransactionsByFinishStationWithLimitAndOffset(UUID finishStationId, Integer limit, Integer offset) {
        return new ResponseEntity<>(
                appTransactionRepository.getTransactionsByFinishStationWithLimitAndOffset(finishStationId, limit, offset), HttpStatus.OK);
    }

    @Override
    public void solveOverdueTransactions() {
        Date date = new Date();
        int hour = date.toInstant().atZone(ZoneId.systemDefault()).getHour();
        if(hour < 7) {
            System.out.println("Checking for long overdue transactions...");
            List<AppTransaction> appTransactionList = appTransactionRepository.getAllUnfinishedTransactions();
            for(AppTransaction appTransaction : appTransactionList) {
                appTransaction.setFinishTime(date);
                appTransaction.setPenalty(300.0);
                appTransactionRepository.save(appTransaction);
                AppUser appUser = appUserRepository.getAppUserById(appTransaction.getUserId());
                appUser.setBanned(true);
                appUserRepository.save(appUser);
                Bicycle bicycle = bicycleRepository.getBicycleById(appTransaction.getBicycleId());
                bicycle.setStatus("Stolen");
                bicycleRepository.save(bicycle);
            }
            System.out.println("Solved the " + appTransactionList.size() + " long overdue transaction(s).");
        }
    }

    @Override
    @Scheduled(cron = "0 15 3 * * *")
    public void onScheduleCallSolveOverdueTransactions() {
        solveOverdueTransactions();
    }

    @Override
    @EventListener(ContextRefreshedEvent.class)
    public void onStartupCallSolveOverdueTransactions() {
        solveOverdueTransactions();
    }
}
