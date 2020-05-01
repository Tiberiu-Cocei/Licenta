package com.thesis.webapi.services.impl;

import com.thesis.webapi.dtos.AppTransactionCreateDto;
import com.thesis.webapi.dtos.AppTransactionPreviewDto;
import com.thesis.webapi.dtos.AppTransactionUpdateDto;
import com.thesis.webapi.entities.AppTransaction;
import com.thesis.webapi.entities.Settings;
import com.thesis.webapi.repositories.AppTransactionRepository;
import com.thesis.webapi.repositories.SettingsRepository;
import com.thesis.webapi.services.AppTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppTransactionServiceImpl implements AppTransactionService {

    private final AppTransactionRepository appTransactionRepository;

    private final SettingsRepository settingsRepository;

    @Autowired
    public AppTransactionServiceImpl(AppTransactionRepository appTransactionRepository, SettingsRepository settingsRepository) {
        this.appTransactionRepository = appTransactionRepository;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public ResponseEntity<List<AppTransaction>> getAppTransactionsByUserId(UUID userId) {
        return new ResponseEntity<>(appTransactionRepository.getAppTransactionsByUserId(userId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> createTransaction(AppTransactionCreateDto appTransactionCreateDto) {
        //validari pentru timp + capacitate
        AppTransaction appTransaction = new AppTransaction(appTransactionCreateDto);
        Settings settings = settingsRepository.getSettingsByCityId(appTransactionCreateDto.getCityId());
        double dateDifference = appTransaction.getPlannedTime().getTime() - appTransaction.getStartTime().getTime();
        double minuteDifference = dateDifference / (60 * 1000);
        Double initialCost = settings.getBasePrice() + Math.ceil(minuteDifference / settings.getIntervalTime()) * settings.getIntervalPrice();
        appTransaction.setInitialCost(initialCost);
        appTransactionRepository.save(appTransaction);
        return new ResponseEntity<>("Successfully created the transaction.", HttpStatus.CREATED);
        //DE TERMINAT
    }

    @Override
    public ResponseEntity<String> finalizeTransaction(AppTransactionUpdateDto appTransactionUpdateDto) {
        return null;
    }

    @Override
    public ResponseEntity<String> previewTransaction(AppTransactionPreviewDto appTransactionPreviewDto) {
        return null;
    }
}
