package com.thesis.webapi.services.impl;

import com.thesis.webapi.entities.City;
import com.thesis.webapi.entities.Settings;
import com.thesis.webapi.repositories.CityRepository;
import com.thesis.webapi.repositories.SettingsRepository;
import com.thesis.webapi.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    private final SettingsRepository settingsRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, SettingsRepository settingsRepository) {
        this.cityRepository = cityRepository;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public ResponseEntity<List<City>> getAll() {
        return new ResponseEntity<>(cityRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Settings> getSettingsByCityId(UUID cityId) {
        Settings settings = settingsRepository.getSettingsByCityId(cityId);
        if(settings != null) {
            return new ResponseEntity<>(settings, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
