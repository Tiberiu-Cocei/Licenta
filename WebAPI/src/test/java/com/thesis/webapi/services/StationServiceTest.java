package com.thesis.webapi.services;

import com.thesis.webapi.entities.Station;
import com.thesis.webapi.repositories.StationRepository;
import com.thesis.webapi.services.impl.StationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StationServiceTest {

    @Mock
    StationRepository stationRepository;

    @InjectMocks
    StationServiceImpl stationService;

    private UUID cityId;

    private ArrayList<Station> stationList;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        cityId = UUID.randomUUID();
        Station firstStation = new Station(cityId, "First");
        Station secondStation = new Station(cityId, "Second");
        stationList = new ArrayList<>();
        stationList.add(firstStation);
        stationList.add(secondStation);
    }

    @Test
    public void whenGetStationsByCityIdIsCalled_WithExistingId_ThenReturnCorrectList() {
        //Arrange
        Mockito.when(stationRepository.getStationsByCityId(cityId)).thenReturn(stationList);

        //Act
        ResponseEntity<List<Station>> stations = stationService.getStationsByCityId(cityId);

        //Assert
        Assertions.assertThat(stations).isNotNull();
        Assertions.assertThat(stations.getBody().size()).isEqualTo(2);
        Assertions.assertThat(stations.getBody().get(0)).isEqualToComparingFieldByFieldRecursively(stationList.get(0));
        Assertions.assertThat(stations.getBody().get(1)).isEqualToComparingFieldByFieldRecursively(stationList.get(1));
        Assertions.assertThat(stations.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenGetStationsByCityIdIsCalled_WithNonexistentId_ThenReturnEmptyList() {
        //Arrange
        Mockito.when(stationRepository.getStationsByCityId(cityId)).thenReturn(stationList);

        //Act
        ResponseEntity<List<Station>> stations = stationService.getStationsByCityId(UUID.randomUUID());

        //Assert
        Assertions.assertThat(stations).isNotNull();
        Assertions.assertThat(stations.getBody().size()).isEqualTo(0);
        Assertions.assertThat(stations.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @After
    public void tearDown(){
        cityId = null;
        stationList = null;
    }

}
