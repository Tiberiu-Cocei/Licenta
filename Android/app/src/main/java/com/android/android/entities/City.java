package com.android.android.entities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

public class City {

    private UUID cityId;

    private String name;

    public static List<City> createCityListFromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<List<City>>(){});
    }

    public UUID getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    public void setCityId(UUID cityId) {
        this.cityId = cityId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
