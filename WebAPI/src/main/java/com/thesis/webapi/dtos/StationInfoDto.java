package com.thesis.webapi.dtos;

import com.thesis.webapi.entities.Station;

import java.util.UUID;

public class StationInfoDto {

    private UUID id;

    private String name;

    private UUID cityId;

    public StationInfoDto(Station station) {
        this.id = station.getId();
        this.name = station.getName();
        this.cityId = station.getCityId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getCityId() {
        return cityId;
    }

    public void setCityId(UUID cityId) {
        this.cityId = cityId;
    }
}
