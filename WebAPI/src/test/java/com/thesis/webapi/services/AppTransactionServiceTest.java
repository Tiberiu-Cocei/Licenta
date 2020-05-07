package com.thesis.webapi.services;

import com.thesis.webapi.dtos.AppTransactionCreateDto;
import com.thesis.webapi.dtos.AppTransactionPreviewDto;
import com.thesis.webapi.dtos.AppTransactionUpdateDto;
import com.thesis.webapi.dtos.AppUserCreateDto;
import com.thesis.webapi.entities.*;
import com.thesis.webapi.repositories.*;
import com.thesis.webapi.services.impl.AppTransactionServiceImpl;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AppTransactionServiceTest {

    @Mock
    AppTransactionRepository appTransactionRepository;

    @Mock
    SettingsRepository settingsRepository;

    @Mock
    StationRepository stationRepository;

    @Mock
    DiscountRepository discountRepository;

    @Mock
    BicycleRepository bicycleRepository;

    @Mock
    AppUserRepository appUserRepository;

    @InjectMocks
    AppTransactionServiceImpl appTransactionService;

    private UUID userId;

    private UUID cityId;

    private Settings settings;

    private Station startStation;

    private Station plannedStation;

    private AppTransactionCreateDto appTransactionCreateDto;

    private Date startTime;

    private Date plannedTime;

    private List<AppTransaction> appTransactions;

    private AppTransactionUpdateDto appTransactionUpdateDto;

    private AppTransactionPreviewDto appTransactionPreviewDto;

    private Bicycle bicycle;

    private AppUser appUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        AppUserCreateDto appUserCreateDto = new AppUserCreateDto("test_username", "12345", "test@gmail.com");
        this.appUser = new AppUser(appUserCreateDto);
        bicycle = new Bicycle(UUID.randomUUID(), "Test");
        bicycle.setStatus("Station");
        this.userId = appUser.getId();
        this.settings = new Settings(cityId, 5.0, 5.0, 5);
        this.cityId = UUID.randomUUID();
        this.startStation = new Station(cityId, "First", 30, 15);
        this.plannedStation = new Station(cityId, "Second", 30, 15);
        this.startTime = new Date();
        this.plannedTime = new Date(startTime.getTime() + 100000);
        this.appTransactionCreateDto = new AppTransactionCreateDto(userId, startStation.getId(), plannedStation.getId(), plannedTime, cityId, bicycle.getId());
        AppTransactionCreateDto appTransactionCreateDtoTwo = new AppTransactionCreateDto(userId, null, null, null, null, bicycle.getId());
        AppTransaction firstAppTransaction = new AppTransaction(appTransactionCreateDto);
        AppTransaction secondAppTransaction = new AppTransaction(appTransactionCreateDtoTwo);
        appTransactions = new ArrayList<>();
        appTransactions.add(firstAppTransaction);
        appTransactions.add(secondAppTransaction);
        this.appTransactionUpdateDto = new AppTransactionUpdateDto(userId, plannedStation.getId());
        this.appTransactionPreviewDto = new AppTransactionPreviewDto(plannedTime, cityId);
    }

    @Test
    public void whenGetAppTransactionsByUserIdIsCalled_WithExistingId_ThenReturnCorrectList() {
        //Arrange
        Mockito.when(appTransactionRepository.getAppTransactionsByUserId(userId)).thenReturn(appTransactions);

        //Act
        ResponseEntity<List<AppTransaction>> transactions = appTransactionService.getAppTransactionsByUserId(userId);

        //Assert
        Assertions.assertThat(transactions).isNotNull();
        Assertions.assertThat(transactions.getBody().size()).isEqualTo(2);
        Assertions.assertThat(transactions.getBody().get(0)).isEqualToComparingFieldByFieldRecursively(appTransactions.get(0));
        Assertions.assertThat(transactions.getBody().get(1)).isEqualToComparingFieldByFieldRecursively(appTransactions.get(1));
        Assertions.assertThat(transactions.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenGetAppTransactionsByUserIdIsCalled_WithNonexistentId_ThenReturnEmptyList() {
        //Arrange
        Mockito.when(appTransactionRepository.getAppTransactionsByUserId(userId)).thenReturn(appTransactions);

        //Act
        ResponseEntity<List<AppTransaction>> transactions = appTransactionService.getAppTransactionsByUserId(UUID.randomUUID());

        //Assert
        Assertions.assertThat(transactions).isNotNull();
        Assertions.assertThat(transactions.getBody().size()).isEqualTo(0);
        Assertions.assertThat(transactions.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenCreateTransactionIsCalled_WithNonexistentCityId_ThenError() {
        //Arrange
        Mockito.when(appUserRepository.getAppUserById(appUser.getId())).thenReturn(appUser);

        //Act
        ResponseEntity<String> response = appTransactionService.createTransaction(appTransactionCreateDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("No city settings found.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenCreateTransactionIsCalled_WithNonexistentBicycleId_ThenError() {
        //Arrange
        Mockito.when(settingsRepository.getSettingsByCityId(cityId)).thenReturn(settings);
        Mockito.when(appUserRepository.getAppUserById(appUser.getId())).thenReturn(appUser);

        //Act
        ResponseEntity<String> response = appTransactionService.createTransaction(appTransactionCreateDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("No bicycle found for given id.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenCreateTransactionIsCalled_WithBicycleThatDoesNotHaveStationStatus_ThenError() {
        //Arrange
        Mockito.when(appUserRepository.getAppUserById(appUser.getId())).thenReturn(appUser);
        Mockito.when(settingsRepository.getSettingsByCityId(cityId)).thenReturn(settings);
        Bicycle invalidBicycle = new Bicycle(UUID.randomUUID(), "Test_invalid");
        invalidBicycle.setStatus("Damaged");
        Mockito.when(bicycleRepository.getBicycleById(invalidBicycle.getId())).thenReturn(invalidBicycle);
        AppTransactionCreateDto wrongAppTransactionCreateDto =
                new AppTransactionCreateDto(userId, startStation.getId(), startStation.getId(), plannedTime, cityId, invalidBicycle.getId());

        //Act
        ResponseEntity<String> response = appTransactionService.createTransaction(wrongAppTransactionCreateDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("Bicycle is not available for transport.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenCreateTransactionIsCalled_WithInvalidPlannedTime_ThenError() {
        //Arrange
        Mockito.when(appUserRepository.getAppUserById(appUser.getId())).thenReturn(appUser);
        Date wrongPlannedTime = new Date(startTime.getTime() - 10000);
        AppTransactionCreateDto wrongAppTransactionCreateDto =
                new AppTransactionCreateDto(userId, startStation.getId(), plannedStation.getId(), wrongPlannedTime, cityId, bicycle.getId());
        Mockito.when(settingsRepository.getSettingsByCityId(cityId)).thenReturn(settings);
        Mockito.when(bicycleRepository.getBicycleById(this.bicycle.getId())).thenReturn(this.bicycle);

        //Act
        ResponseEntity<String> response = appTransactionService.createTransaction(wrongAppTransactionCreateDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("Planned finish time cannot be in the past.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenCreateTransactionIsCalled_WithEmptyStartStation_ThenError() {
        //Arrange
        Mockito.when(appUserRepository.getAppUserById(appUser.getId())).thenReturn(appUser);
        Station emptyStation = new Station(cityId, "Empty", 30, 0);
        AppTransactionCreateDto wrongAppTransactionCreateDto =
                new AppTransactionCreateDto(userId, emptyStation.getId(), plannedStation.getId(), plannedTime, cityId, bicycle.getId());
        Mockito.when(settingsRepository.getSettingsByCityId(cityId)).thenReturn(settings);
        Mockito.when(stationRepository.getStationById(emptyStation.getId())).thenReturn(emptyStation);
        Mockito.when(bicycleRepository.getBicycleById(this.bicycle.getId())).thenReturn(this.bicycle);

        //Act
        ResponseEntity<String> response = appTransactionService.createTransaction(wrongAppTransactionCreateDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("There are no available bicycles in the start station.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenCreateTransactionIsCalled_WithFullPlannedStation_ThenError() {
        //Arrange
        Mockito.when(appUserRepository.getAppUserById(appUser.getId())).thenReturn(appUser);
        Station fullStation = new Station(cityId, "Full", 30, 30);
        AppTransactionCreateDto wrongAppTransactionCreateDto =
                new AppTransactionCreateDto(userId, startStation.getId(), fullStation.getId(), plannedTime, cityId, bicycle.getId());
        Mockito.when(settingsRepository.getSettingsByCityId(cityId)).thenReturn(settings);
        Mockito.when(stationRepository.getStationById(startStation.getId())).thenReturn(startStation);
        Mockito.when(stationRepository.getStationById(fullStation.getId())).thenReturn(fullStation);
        Mockito.when(bicycleRepository.getBicycleById(this.bicycle.getId())).thenReturn(this.bicycle);

        //Act
        ResponseEntity<String> response = appTransactionService.createTransaction(wrongAppTransactionCreateDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("Planned station is at maximum capacity.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenCreateTransactionIsCalled_WithValidValues_ThenSucceed() {
        //Arrange
        Mockito.when(appUserRepository.getAppUserById(appUser.getId())).thenReturn(appUser);
        Mockito.when(settingsRepository.getSettingsByCityId(cityId)).thenReturn(settings);
        Mockito.when(stationRepository.getStationById(startStation.getId())).thenReturn(startStation);
        Mockito.when(stationRepository.getStationById(plannedStation.getId())).thenReturn(plannedStation);
        Mockito.when(bicycleRepository.getBicycleById(this.bicycle.getId())).thenReturn(this.bicycle);

        //Act
        ResponseEntity<String> response = appTransactionService.createTransaction(appTransactionCreateDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("Successfully created the transaction.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    }

    @Test
    public void whenFinalizeTransactionIsCalled_WithNoActiveTransactions_ThenError() {
        //Arrange
        Mockito.when(appTransactionRepository.getUnfinishedTransactionsByUser(userId)).thenReturn(new ArrayList<>());

        //Act
        ResponseEntity<String> response = appTransactionService.finalizeTransaction(appTransactionUpdateDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("There are no transactions to finalize.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenFinalizeTransactionIsCalled_WithTooManyActiveTransactions_ThenError() {
        //Arrange
        ArrayList<AppTransaction> appTransactions = new ArrayList<>();
        appTransactions.add(this.appTransactions.get(0));
        appTransactions.add(this.appTransactions.get(0));
        Mockito.when(appTransactionRepository.getUnfinishedTransactionsByUser(userId)).thenReturn(appTransactions);

        //Act
        ResponseEntity<String> response = appTransactionService.finalizeTransaction(appTransactionUpdateDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("There is more than a transaction to finalize. Please contact an administrator.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenFinalizeTransactionIsCalled_WithFullFinishStation_ThenError() {
        //Arrange
        ArrayList<AppTransaction> appTransactions = new ArrayList<>();
        appTransactions.add(this.appTransactions.get(0));
        Station fullStation = new Station(cityId, "Full", 30, 30);
        AppTransactionUpdateDto wrongAppTransactionUpdateDto = new AppTransactionUpdateDto(userId, fullStation.getId());
        Mockito.when(appTransactionRepository.getUnfinishedTransactionsByUser(userId)).thenReturn(appTransactions);
        Mockito.when(stationRepository.getStationById(fullStation.getId())).thenReturn(fullStation);

        //Act
        ResponseEntity<String> response = appTransactionService.finalizeTransaction(wrongAppTransactionUpdateDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("Cannot finalize transaction. Station is at full capacity.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenFinalizeTransactionIsCalled_WithValidValues_ThenSucceed() {
        //Arrange
        ArrayList<AppTransaction> appTransactions = new ArrayList<>();
        appTransactions.add(this.appTransactions.get(0));
        Mockito.when(appTransactionRepository.getUnfinishedTransactionsByUser(userId)).thenReturn(appTransactions);
        Mockito.when(stationRepository.getStationById(plannedStation.getId())).thenReturn(plannedStation);

        //Act
        ResponseEntity<String> response = appTransactionService.finalizeTransaction(appTransactionUpdateDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("Successfully finished transaction");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenPreviewTransactionIsCalled_WithInvalidCityId_ThenError() {
        //Arrange

        //Act
        ResponseEntity<Double> response = appTransactionService.previewTransaction(appTransactionPreviewDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isNull();
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenPreviewTransactionIsCalled_WithInvalidTimes_ThenError() {
        //Arrange
        Date wrongPlannedTime = new Date(startTime.getTime() - 1000);
        AppTransactionPreviewDto wrongAppTransactionPreviewDto = new AppTransactionPreviewDto(wrongPlannedTime, cityId);
        Mockito.when(settingsRepository.getSettingsByCityId(cityId)).thenReturn(settings);

        //Act
        ResponseEntity<Double> response = appTransactionService.previewTransaction(wrongAppTransactionPreviewDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isNull();
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenPreviewTransactionIsCalled_WithValidValues_ThenSucceed() {
        //Arrange
        Mockito.when(settingsRepository.getSettingsByCityId(cityId)).thenReturn(settings);

        //Act
        ResponseEntity<Double> response = appTransactionService.previewTransaction(appTransactionPreviewDto);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @After
    public void tearDown() {
        userId = null;
        settings = null;
        startStation = null;
        plannedStation = null;
        cityId = null;
        appTransactionCreateDto = null;
        startTime = null;
        plannedTime = null;
        appTransactions = null;
        appTransactionUpdateDto = null;
        appTransactionPreviewDto = null;
        bicycle = null;
        appUser = null;
    }

}
