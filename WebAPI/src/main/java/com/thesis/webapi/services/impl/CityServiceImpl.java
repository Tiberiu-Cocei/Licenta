package com.thesis.webapi.services.impl;

import com.thesis.webapi.entities.City;
import com.thesis.webapi.repositories.CityRepository;
import com.thesis.webapi.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<City> getAll() {
        return cityRepository.findAll();
    }

}
