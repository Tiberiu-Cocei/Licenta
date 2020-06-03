package com.thesis.webapi.controllers;

import com.thesis.webapi.dtos.SettingsUpdateDto;
import com.thesis.webapi.entities.City;
import com.thesis.webapi.entities.Settings;
import com.thesis.webapi.services.CityService;
import com.thesis.webapi.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api")
public class CityController {

    private final CityService cityService;

    private final SettingsService settingsService;

    @Autowired
    public CityController(CityService cityService, SettingsService settingsService) {
        this.cityService = cityService;
        this.settingsService = settingsService;
    }

    @GetMapping(value = "/unsecure/cities")
    public ResponseEntity<List<City>> getAllCities(){
        return cityService.getAll();
    }

    @GetMapping(value = "/admin/settings/{id}")
    public ResponseEntity<Settings> getSettingsByCityId(@PathVariable("id") UUID cityId) {
        return settingsService.getSettingsByCityId(cityId);
    }

    @PostMapping(value = "/admin/settings")
    public ResponseEntity<String> updateSettings(@Valid @RequestBody SettingsUpdateDto settingsUpdateDto) {
        return settingsService.updateSettings(settingsUpdateDto);
    }

}
