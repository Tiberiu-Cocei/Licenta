package com.thesis.webapi.services;

import com.thesis.webapi.dtos.ReportCreateDto;
import com.thesis.webapi.entities.Bicycle;
import com.thesis.webapi.entities.Report;
import com.thesis.webapi.repositories.BicycleRepository;
import com.thesis.webapi.repositories.ReportRepository;
import com.thesis.webapi.services.impl.BicycleServiceImpl;
import com.thesis.webapi.services.impl.ReportServiceImpl;
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

public class BicycleServiceTest {

    @Mock
    BicycleRepository bicycleRepository;

    @InjectMocks
    BicycleServiceImpl bicycleService;

    private UUID stationId;

    private ArrayList<Bicycle> bicycleList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        stationId = UUID.randomUUID();
        Bicycle firstBicycle = new Bicycle(UUID.randomUUID(), "First");
        Bicycle secondBicycle = new Bicycle(UUID.randomUUID(), "Second");
        bicycleList = new ArrayList<>();
        bicycleList.add(firstBicycle);
        bicycleList.add(secondBicycle);
    }

    @Test
    public void whenGetBicyclesByStationIdIsCalled_WithExistingId_ThenReturnCorrectList() {
        //Arrange
        Mockito.when(bicycleRepository.getBicyclesByStationId(stationId)).thenReturn(bicycleList);

        //Act
        ResponseEntity<List<Bicycle>> bicycles = bicycleService.getBicyclesByStationId(stationId);

        //Assert
        Assertions.assertThat(bicycles).isNotNull();
        Assertions.assertThat(bicycles.getBody().size()).isEqualTo(2);
        Assertions.assertThat(bicycles.getBody().get(0)).isEqualToComparingFieldByFieldRecursively(bicycleList.get(0));
        Assertions.assertThat(bicycles.getBody().get(1)).isEqualToComparingFieldByFieldRecursively(bicycleList.get(1));
        Assertions.assertThat(bicycles.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenGetBicyclesByStationIdIsCalled_WithNonexistentId_ThenReturnEmptyList() {
        //Arrange
        Mockito.when(bicycleRepository.getBicyclesByStationId(stationId)).thenReturn(bicycleList);

        //Act
        ResponseEntity<List<Bicycle>> bicycles = bicycleService.getBicyclesByStationId(UUID.randomUUID());

        //Assert
        Assertions.assertThat(bicycles).isNotNull();
        Assertions.assertThat(bicycles.getBody().size()).isEqualTo(0);
        Assertions.assertThat(bicycles.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenGetBicycleByIdIsCalled_WithExistingId_ThenReturnCorrectReport() {
        //Arrange
        Mockito.when(bicycleRepository.getBicycleById(this.bicycleList.get(0).getId())).thenReturn(this.bicycleList.get(0));

        //Act
        ResponseEntity<Bicycle> bicycle = bicycleService.getBicycleById(this.bicycleList.get(0).getId());

        //Assert
        Assertions.assertThat(bicycle).isNotNull();
        Assertions.assertThat(bicycle.getBody()).isEqualToComparingFieldByFieldRecursively(this.bicycleList.get(0));
        Assertions.assertThat(bicycle.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenGetBicycleByIdIsCalled_WithNonexistentId_ThenError() {
        //Arrange

        //Act
        ResponseEntity<Bicycle> bicycle = bicycleService.getBicycleById(UUID.randomUUID());

        //Assert
        Assertions.assertThat(bicycle).isNotNull();
        Assertions.assertThat(bicycle.getBody()).isNull();
        Assertions.assertThat(bicycle.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    }

    @After
    public void tearDown() {
        stationId = null;
        bicycleList = null;
    }

}
