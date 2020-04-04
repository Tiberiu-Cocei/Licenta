package com.thesis.webapi.controllers;

import com.thesis.webapi.entities.City;
import com.thesis.webapi.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public List<City> getAllCities(){
        return cityService.getAll();
    }

}
