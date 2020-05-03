package com.thesis.webapi.services;

import com.thesis.webapi.entities.City;
import com.thesis.webapi.entities.Settings;
import com.thesis.webapi.repositories.CityRepository;
import com.thesis.webapi.repositories.SettingsRepository;
import com.thesis.webapi.services.impl.CityServiceImpl;
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

public class CityServiceTest {

    @Mock
    CityRepository cityRepository;

    @Mock
    SettingsRepository settingsRepository;

    @InjectMocks
    CityServiceImpl cityService;

    private UUID cityId;

    private ArrayList<City> cityList;

    private Settings settings;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        cityId = UUID.randomUUID();
        City firstCity = new City(UUID.randomUUID(), "First");
        City secondCity = new City(UUID.randomUUID(), "Second");
        cityList = new ArrayList<>();
        cityList.add(firstCity);
        cityList.add(secondCity);
        settings = new Settings();
    }

    @Test
    public void whenGetAllIsCalled_ThenReturnCorrectList() {
        //Arrange
        Mockito.when(cityRepository.findAll()).thenReturn(cityList);

        //Act
        ResponseEntity<List<City>> cities = cityService.getAll();

        //Assert
        Assertions.assertThat(cities).isNotNull();
        Assertions.assertThat(cities.getBody().size()).isEqualTo(2);
        Assertions.assertThat(cities.getBody().get(0)).isEqualToComparingFieldByFieldRecursively(cityList.get(0));
        Assertions.assertThat(cities.getBody().get(1)).isEqualToComparingFieldByFieldRecursively(cityList.get(1));
        Assertions.assertThat(cities.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenGetSettingsByCityIdIsCalled_WithExistingId_ThenReturnSettings() {
        //Arrange
        Mockito.when(settingsRepository.getSettingsByCityId(cityId)).thenReturn(this.settings);

        //Act
        ResponseEntity<Settings> settings = cityService.getSettingsByCityId(cityId);

        //Assert
        Assertions.assertThat(settings).isNotNull();
        Assertions.assertThat(settings.getBody()).isEqualTo(this.settings);
        Assertions.assertThat(settings.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenGetSettingsByCityIdIsCalled_WithNonexistentId_ThenError() {
        //Arrange

        //Act
        ResponseEntity<Settings> settings = cityService.getSettingsByCityId(UUID.randomUUID());

        //Assert
        Assertions.assertThat(settings).isNotNull();
        Assertions.assertThat(settings.getBody()).isNull();
        Assertions.assertThat(settings.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    }

    @After
    public void tearDown() {
        cityId = null;
        cityList = null;
        settings = null;
    }

}
