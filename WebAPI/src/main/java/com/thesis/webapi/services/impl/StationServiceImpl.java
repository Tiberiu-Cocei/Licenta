package com.thesis.webapi.services.impl;

import com.thesis.webapi.dtos.StationInfoDto;
import com.thesis.webapi.entities.Station;
import com.thesis.webapi.repositories.StationRepository;
import com.thesis.webapi.services.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;

    @Autowired
    public StationServiceImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public ResponseEntity<List<Station>> getStationsByCityId(UUID cityId) {
        return new ResponseEntity<>(stationRepository.getStationsByCityId(cityId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Station> getStationById(UUID stationId) {
        return new ResponseEntity<>(stationRepository.getStationById(stationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<StationInfoDto>> getStationInfoByCityId(UUID cityId) {
        List<Station> stationList = stationRepository.getStationsAndWarehouseByCityId(cityId);
        List<StationInfoDto> stationInfoDtoList = new ArrayList<>();
        for(Station station : stationList) {
            StationInfoDto stationInfoDto = new StationInfoDto(station);
            stationInfoDtoList.add(stationInfoDto);
        }
        return new ResponseEntity<>(stationInfoDtoList, HttpStatus.OK);
    }
}
