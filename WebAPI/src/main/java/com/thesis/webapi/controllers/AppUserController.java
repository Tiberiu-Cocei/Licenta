package com.thesis.webapi.controllers;

import com.thesis.webapi.dtos.*;
import com.thesis.webapi.entities.PaymentMethod;
import com.thesis.webapi.security.PasswordHashing;
import com.thesis.webapi.services.AppUserService;
import com.thesis.webapi.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api")
public class AppUserController {

    private final AppUserService appUserService;

    private final PasswordHashing passwordHashing;

    private final EmailService emailService;

    @Autowired
    public AppUserController(AppUserService appUserService, EmailService emailService) {
        this.appUserService = appUserService;
        this.passwordHashing = new PasswordHashing();
        this.emailService = emailService;
    }

    @PostMapping(value = "/unsecure/users/register")
    public ResponseEntity<String> register(@Valid @RequestBody AppUserCreateDto appUserCreateDto) {
        return appUserService.saveAppUser(appUserCreateDto, passwordHashing);
    }

    @PostMapping(value = "/unsecure/users/login")
    public ResponseEntity<AppUserLoggedInDto> login(@Valid @RequestBody AppUserLoginDto appUserLoginDto) {
        return appUserService.login(appUserLoginDto.getUsername(), appUserLoginDto.getPassword(), passwordHashing);
    }

    @Async
    @PostMapping(value = "/unsecure/users/send-reset-code")
    public void sendResetCode(@Valid @RequestBody AppUserResetCodeDto appUserResetCodeDto) {
        appUserService.sendResetCode(appUserResetCodeDto, emailService);
    }

    @PostMapping(value = "/unsecure/users/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody AppUserResetPasswordDto appUserResetPasswordDto) {
        return appUserService.resetPassword(appUserResetPasswordDto, passwordHashing);
    }

    @PutMapping(value = "/secure/users/payment-methods/{id}")
    public ResponseEntity<UUID> savePaymentMethod(
            @PathVariable("id") UUID userId,
            @Valid @RequestBody PaymentMethodCreateDto paymentMethodCreateDto) {
        return appUserService.savePaymentMethod(userId, paymentMethodCreateDto);
    }

    @PutMapping(value = "/secure/users/modify")
    public ResponseEntity<AppUserLoggedInDto> modifyAppUser(@Valid @RequestBody AppUserUpdateDto appUserUpdateDto) {
        return appUserService.modifyAppUser(appUserUpdateDto, passwordHashing);
    }

}
