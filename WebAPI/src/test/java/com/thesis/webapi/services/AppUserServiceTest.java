package com.thesis.webapi.services;

import com.thesis.webapi.dtos.*;
import com.thesis.webapi.entities.AppUser;
import com.thesis.webapi.entities.PaymentMethod;
import org.springframework.security.core.userdetails.User;
import com.thesis.webapi.repositories.AppUserRepository;
import com.thesis.webapi.repositories.PaymentMethodRepository;
import com.thesis.webapi.security.PasswordHashing;
import com.thesis.webapi.services.impl.AppUserServiceImpl;
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

import java.util.Optional;
import java.util.UUID;

public class AppUserServiceTest {

    @Mock
    AppUserRepository appUserRepository;

    @Mock
    PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    AppUserServiceImpl appUserService;

    private AppUserCreateDto appUserCreateDto;

    private AppUser appUser;

    private AppUserUpdateDto appUserUpdateDto;

    private PaymentMethodCreateDto paymentMethodCreateDto;

    private PasswordHashing passwordHashing;

    private AppUserResetPasswordDto appUserResetPasswordDto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.appUserCreateDto = new AppUserCreateDto("test_username", "12345", "test@gmail.com");
        this.appUser = new AppUser(appUserCreateDto);
        this.paymentMethodCreateDto = new PaymentMethodCreateDto("Test", null, null, null);
        this.passwordHashing = new PasswordHashing();
        this.appUser.setAuthenticationToken(UUID.randomUUID());
        this.appUserUpdateDto = new AppUserUpdateDto("test_username", "12345", "test@gmail.com");
        this.appUserResetPasswordDto = new AppUserResetPasswordDto("test_username", "65432", "123456");
        this.appUser.setPasswordResetCode("65432");
        this.appUser.setSalt("\u0014r�<[H�k�ޮ�\u0003�iR");
        this.appUser.setPaymentMethod(new PaymentMethod(UUID.randomUUID(), paymentMethodCreateDto));
    }

    @Test
    public void whenLoginIsCalled_WithValidValues_ThenReturnCorrectResponse() {
        //Arrange
        Mockito.when(appUserRepository.login(appUser.getUsername(), "q�\u0015\u0015Ɋ-^L\u001F�\u001E7�{�")).thenReturn(appUser);
        Mockito.when(appUserRepository.getSalt(appUser.getUsername())).thenReturn("\u0014r�<[H�k�ޮ�\u0003�iR");

        //Act
        ResponseEntity<AppUserLoggedInDto> appUserLoggedIn = appUserService.login(appUser.getUsername(), appUser.getPassword(), passwordHashing);

        //Assert
        Assertions.assertThat(appUserLoggedIn).isNotNull();
        Assertions.assertThat(appUserLoggedIn.getBody().getUsername()).isEqualTo("test_username");
        Assertions.assertThat(appUserLoggedIn.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenLoginIsCalled_WithInvalidValues_ThenReturnUnauthorizedResponse() {
        //Arrange
        Mockito.when(appUserRepository.login(appUser.getUsername(), "q�\u0015\u0015Ɋ-^L\u001F�\u001E7�{�")).thenReturn(appUser);
        Mockito.when(appUserRepository.getSalt(appUser.getUsername())).thenReturn("\u0014r�<[H�k�ޮ�\u0003�iR");

        //Act
        ResponseEntity<AppUserLoggedInDto> appUserLoggedIn = appUserService.login("wrong_username", "wrong_password", passwordHashing);

        //Assert
        Assertions.assertThat(appUserLoggedIn).isNotNull();
        Assertions.assertThat(appUserLoggedIn.getBody()).isNull();
        Assertions.assertThat(appUserLoggedIn.getStatusCode()).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void whenFindUserByAuthenticationTokenIsCalled_WithExistingValue_ThenReturnUser() {
        //Arrange
        Optional<AppUser> appUserOptional = Optional.of(appUser);
        Mockito.when(appUserRepository.findUserByAuthenticationToken(appUser.getAuthenticationToken())).thenReturn(appUserOptional);

        //Act
        Optional<User> userOptional = appUserService.findUserByAuthenticationToken(appUser.getAuthenticationToken().toString());

        //Assert
        Assertions.assertThat(userOptional.isPresent()).isTrue();
        Assertions.assertThat(userOptional.get().getUsername()).isEqualTo("test_username");
    }

    @Test
    public void whenFindUserByAuthenticationTokenIsCalled_WithNonexistentValue_ThenReturnOptionalEmpty() {
        //Arrange
        Optional<AppUser> appUserOptional = Optional.of(appUser);
        Mockito.when(appUserRepository.findUserByAuthenticationToken(appUser.getAuthenticationToken())).thenReturn(appUserOptional);

        //Act
        Optional<User> userOptional = appUserService.findUserByAuthenticationToken(UUID.randomUUID().toString());

        //Assert
        Assertions.assertThat(userOptional.isPresent()).isFalse();
    }

    @Test
    public void whenSaveAppUserIsCalled_WithUsernameTaken_ThenReturnMessage() {
        //Arrange
        Mockito.when(appUserRepository.isUsernameTaken("test_username")).thenReturn("test_username");

        //Act
        ResponseEntity<String> response = appUserService.register(appUserCreateDto, passwordHashing);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("Username is already in use.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenSaveAppUserIsCalled_WithEmailTaken_ThenReturnMessage() {
        //Arrange
        Mockito.when(appUserRepository.isEmailTaken("test@gmail.com")).thenReturn("test@gmail.com");

        //Act
        ResponseEntity<String> response = appUserService.register(appUserCreateDto, passwordHashing);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("Email is already in use.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenSaveAppUserIsCalled_WithUniqueValues_ThenReturnCreatedResponse() {
        //Arrange

        //Act
        ResponseEntity<String> response = appUserService.register(appUserCreateDto, passwordHashing);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("Successfully created user.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    }

    @Test
    public void whenSavePaymentMethodIsCalled_WithInvalidUserId_ThenReturnNotFoundResponse() {
        //Arrange

        //Act
        ResponseEntity<UUID> paymentMethodId = appUserService.savePaymentMethod(null, this.paymentMethodCreateDto);

        //Assert
        Assertions.assertThat(paymentMethodId).isNotNull();
        Assertions.assertThat(paymentMethodId.getBody()).isNull();
        Assertions.assertThat(paymentMethodId.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenSavePaymentMethodIsCalled_WithValidUserId_ThenReturnOkResponse() {
        //Arrange
        Mockito.when(appUserRepository.getAppUserById(appUser.getId())).thenReturn(appUser);

        //Act
        ResponseEntity<UUID> paymentMethodId = appUserService.savePaymentMethod(appUser.getId(), this.paymentMethodCreateDto);

        //Assert
        Assertions.assertThat(paymentMethodId).isNotNull();
        Assertions.assertThat(paymentMethodId.getBody()).isNotNull();
        Assertions.assertThat(paymentMethodId.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenModifyAppUserIsCalled_WithValidValues_ThenReturnOkResponse() {
        //Arrange
        Mockito.when(appUserRepository.login(appUser.getUsername(), "q�\u0015\u0015Ɋ-^L\u001F�\u001E7�{�")).thenReturn(appUser);
        Mockito.when(appUserRepository.getSalt(appUser.getUsername())).thenReturn("\u0014r�<[H�k�ޮ�\u0003�iR");

        //Act
        ResponseEntity<AppUserLoggedInDto> userLoggedIn = appUserService.modifyAppUser(appUserUpdateDto, passwordHashing);

        //Assert
        Assertions.assertThat(userLoggedIn).isNotNull();
        Assertions.assertThat(userLoggedIn.getBody()).isNotNull();
        Assertions.assertThat(userLoggedIn.getBody().getUsername()).isEqualTo("test_username");
        Assertions.assertThat(userLoggedIn.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenModifyAppUserIsCalled_WithInvalidValues_ThenReturnBadRequestResponse() {
        //Arrange

        //Act
        ResponseEntity<AppUserLoggedInDto> userLoggedIn = appUserService.modifyAppUser(appUserUpdateDto, passwordHashing);

        //Assert
        Assertions.assertThat(userLoggedIn).isNotNull();
        Assertions.assertThat(userLoggedIn.getBody()).isNull();
        Assertions.assertThat(userLoggedIn.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenResetPasswordIsCalled_WithValidValues_ThenReturnOkResponse() {
        //Arrange
        Mockito.when(appUserRepository.getAppUserByUsername(appUserResetPasswordDto.getUsername())).thenReturn(appUser);

        //Act
        ResponseEntity<String> response = appUserService.resetPassword(appUserResetPasswordDto, passwordHashing);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("Successfully changed password.");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenResetPasswordIsCalled_WithInvalidValues_ThenReturnBadRequestResponse() {
        //Arrange

        //Act
        ResponseEntity<String> response = appUserService.resetPassword(appUserResetPasswordDto, passwordHashing);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("Wrong username or reset code");
        Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @After
    public void tearDown() {
        appUserCreateDto = null;
        paymentMethodCreateDto = null;
        appUser = null;
        passwordHashing = null;
        appUserUpdateDto = null;
    }

}
