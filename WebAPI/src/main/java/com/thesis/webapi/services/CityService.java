package com.thesis.webapi.services;

import com.thesis.webapi.entities.City;
import com.thesis.webapi.entities.Settings;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CityService {

    ResponseEntity<List<City>> getAll();

    ResponseEntity<Settings> getSettingsByCityId(UUID cityId);

}
