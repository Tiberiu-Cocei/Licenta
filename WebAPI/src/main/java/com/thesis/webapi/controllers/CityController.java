package com.thesis.webapi.controllers;

import com.thesis.webapi.entities.City;
import com.thesis.webapi.entities.Settings;
import com.thesis.webapi.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/secure/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<City>> getAllCities(){
        return cityService.getAll();
    }

    @GetMapping(value = "/settings/{id}")
    public ResponseEntity<Settings> getSettingsByCityId(@PathVariable("id") UUID cityId) {
        return cityService.getSettingsByCityId(cityId);
    }

}
