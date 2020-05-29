package com.thesis.webapi.services;

import com.thesis.webapi.entities.Station;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface StationService {

    ResponseEntity<List<Station>> getStationsByCityId(UUID cityId);

    ResponseEntity<Station> getStationById(UUID stationId);

}
