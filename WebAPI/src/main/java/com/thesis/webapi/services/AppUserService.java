package com.thesis.webapi.services;

import com.thesis.webapi.dtos.*;
import com.thesis.webapi.entities.PaymentMethod;
import com.thesis.webapi.security.PasswordHashing;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;
import java.util.UUID;

public interface AppUserService {

    ResponseEntity<AppUserLoggedInDto> login(String username, String password, PasswordHashing passwordHashing);

    Optional<User> findUserByAuthenticationToken(String authenticationToken);

    ResponseEntity<String> register(AppUserCreateDto appUserCreateDto, PasswordHashing passwordHashing);

    ResponseEntity<UUID> savePaymentMethod(UUID userId, PaymentMethodCreateDto paymentMethodCreateDto);

    ResponseEntity<AppUserLoggedInDto> modifyAppUser(AppUserUpdateDto appUserUpdateDto, PasswordHashing passwordHashing);

    void sendResetCode(AppUserResetCodeDto appUserResetCodeDto, EmailService emailService);

    ResponseEntity<String> resetPassword(AppUserResetPasswordDto appUserResetPasswordDto, PasswordHashing passwordHashing);

}
