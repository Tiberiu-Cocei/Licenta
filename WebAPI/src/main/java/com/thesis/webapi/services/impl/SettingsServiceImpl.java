package com.thesis.webapi.services.impl;

import com.thesis.webapi.dtos.SettingsUpdateDto;
import com.thesis.webapi.entities.Settings;
import com.thesis.webapi.repositories.SettingsRepository;
import com.thesis.webapi.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository settingsRepository;

    @Autowired
    public SettingsServiceImpl(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public ResponseEntity<Settings> getSettingsByCityId(UUID cityId) {
        return new ResponseEntity<>(settingsRepository.getSettingsByCityId(cityId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateSettings(SettingsUpdateDto settingsUpdateDto) {
        Settings settings = settingsRepository.getSettingsByCityId(settingsUpdateDto.getCityId());
        if(settings == null) {
            return new ResponseEntity<>("No settings found for given city id.", HttpStatus.BAD_REQUEST);
        }

        Double basePrice = settingsUpdateDto.getBasePrice();
        if(basePrice != null) {
            if(basePrice < 0) {
                return new ResponseEntity<>("Base price cannot be negative.", HttpStatus.BAD_REQUEST);
            }
            settings.setBasePrice(basePrice);
        }

        Double intervalPrice = settingsUpdateDto.getIntervalPrice();
        if(intervalPrice != null) {
            if(intervalPrice < 0) {
                return new ResponseEntity<>("Interval price cannot be negative.", HttpStatus.BAD_REQUEST);
            }
            settings.setIntervalPrice(intervalPrice);
        }

        Integer intervalTime = settingsUpdateDto.getIntervalTime();
        if(intervalTime != null) {
            if(intervalTime < 0) {
                return new ResponseEntity<>("Interval time cannot be negative.", HttpStatus.BAD_REQUEST);
            }
            settings.setIntervalTime(intervalTime);
        }

        Boolean discountsUsed = settingsUpdateDto.getDiscountsUsed();
        if(discountsUsed != null) {
            settings.setDiscountsUsed(discountsUsed);
        }

        Double discountValue = settingsUpdateDto.getDiscountValue();
        if(discountValue != null) {
            if (discountValue <= 0 || discountValue >= 50) {
                return new ResponseEntity<>("Discount value must be within 0 and 50.", HttpStatus.BAD_REQUEST);
            }
            settings.setDiscountValue(discountValue);
        }

        Boolean transportsUsed = settingsUpdateDto.getTransportsUsed();
        if(transportsUsed != null) {
            settings.setTransportsUsed(transportsUsed);
        }

        settingsRepository.save(settings);

        return new ResponseEntity<>("Successfully updated city settings.", HttpStatus.OK);
    }
}
