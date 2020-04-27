package com.thesis.webapi.services.impl;

import com.thesis.webapi.dtos.*;
import com.thesis.webapi.entities.AppUser;
import com.thesis.webapi.entities.PaymentMethod;
import com.thesis.webapi.repositories.PaymentMethodRepository;
import com.thesis.webapi.security.PasswordHashing;
import com.thesis.webapi.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import com.thesis.webapi.repositories.AppUserRepository;
import com.thesis.webapi.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final Random random;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository, PaymentMethodRepository paymentMethodRepository) {
        this.appUserRepository = appUserRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.random = new Random();
    }

    @Override
    public ResponseEntity<AppUserLoggedInDto> login(String username, String password, PasswordHashing passwordHashing) {
        try {
            String salt = appUserRepository.getSalt(username);
            AppUser appUser = appUserRepository.login(username, passwordHashing.hashString(password, salt));
            if (appUser != null) {
                UUID authenticationToken = UUID.randomUUID();
                appUser.setAuthenticationToken(authenticationToken);
                appUserRepository.save(appUser);
                AppUserLoggedInDto appUserLoggedInDto = new AppUserLoggedInDto(appUser);
                return new ResponseEntity<>(appUserLoggedInDto, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<User> findUserByAuthenticationToken(String authenticationToken) {
        Optional<AppUser> appUserOpt = appUserRepository.findUserByAuthenticationToken(UUID.fromString(authenticationToken));
        if(appUserOpt.isPresent()){
            AppUser appUser = appUserOpt.get();
            User user = new User(appUser.getUsername(), appUser.getPassword(), true, true,
                    true, true, AuthorityUtils.createAuthorityList("USER"));
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public ResponseEntity<String> saveAppUser(AppUserCreateDto appUserCreateDto, PasswordHashing passwordHashing) {
        if(appUserRepository.isUsernameTaken(appUserCreateDto.getUsername()) != null) {
            return new ResponseEntity<>("Username is already in use.", HttpStatus.OK);
        }
        if(appUserRepository.isEmailTaken(appUserCreateDto.getEmail()) != null) {
            return new ResponseEntity<>("Email is already in use.", HttpStatus.OK);
        }
        AppUser appUser = new AppUser(appUserCreateDto);
        appUser.setSalt(passwordHashing.generateSalt());
        try {
            appUser.setPassword(passwordHashing.hashString(appUser.getPassword(), appUser.getSalt()));
            appUserRepository.save(appUser);
            return new ResponseEntity<>("Successfully created user.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create new user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<PaymentMethod> savePaymentMethod(UUID userId, PaymentMethodCreateDto paymentMethodCreateDto) {
        AppUser appUser = appUserRepository.getAppUserById(userId);
        if(appUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        UUID paymentMethodId;
        if(appUser.getPaymentMethod() == null) {
            paymentMethodId = UUID.randomUUID();
        }
        else {
            paymentMethodId = appUser.getPaymentMethod().getId();
        }
        PaymentMethod paymentMethod = new PaymentMethod(paymentMethodId, paymentMethodCreateDto);
        paymentMethodRepository.save(paymentMethod);
        appUser.setPaymentMethod(paymentMethod);
        appUserRepository.save(appUser);
        return new ResponseEntity<>(paymentMethod, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AppUserLoggedInDto> modifyAppUser(AppUserUpdateDto appUserUpdateDto, PasswordHashing passwordHashing) {
        try {
            String salt = appUserRepository.getSalt(appUserUpdateDto.getUsername());
            String password = passwordHashing.hashString(appUserUpdateDto.getOldPassword(), salt);
            AppUser appUser = appUserRepository.login(appUserUpdateDto.getUsername(), password);
            if(appUser != null) {
                if(appUserUpdateDto.getEmail() != null) {
                    appUser.setEmail(appUserUpdateDto.getEmail());
                }
                if(appUserUpdateDto.getNewPassword() != null) {
                    String newPassword = passwordHashing.hashString(appUserUpdateDto.getNewPassword(), salt);
                    appUser.setPassword(newPassword);
                }
                appUserRepository.save(appUser);
                AppUserLoggedInDto appUserLoggedInDto = new AppUserLoggedInDto(appUser);
                return new ResponseEntity<>(appUserLoggedInDto, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void sendResetCode(AppUserResetCodeDto appUserResetCodeDto, EmailService emailService) {
        int randomNumber = random.nextInt(100000);
        String passwordResetCode = String.format("%05d", randomNumber);
        AppUser appUser = appUserRepository.getAppUserByUsername(appUserResetCodeDto.getUsername());
        appUser.setPasswordResetCode(passwordResetCode);
        appUserRepository.save(appUser);
        emailService.sendEmail(appUser.getEmail(), "Your app password reset code", passwordResetCode);
    }

    @Override
    public ResponseEntity<String> resetPassword(AppUserResetPasswordDto appUserResetPasswordDto, PasswordHashing passwordHashing) {
        try {
            AppUser appUser = appUserRepository.getAppUserByUsername(appUserResetPasswordDto.getUsername());
            if (appUser != null && appUser.getPasswordResetCode() != null) {
                if (appUser.getPasswordResetCode().equals(appUserResetPasswordDto.getPasswordResetCode())) {
                    String newPassword = passwordHashing.hashString(appUserResetPasswordDto.getNewPassword(), appUser.getSalt());
                    appUser.setPassword(newPassword);
                    appUser.setPasswordResetCode(null);
                    appUserRepository.save(appUser);
                    return new ResponseEntity<>("Successfully changed password.", HttpStatus.OK);
                }
            }
            return new ResponseEntity<>("Wrong username or reset code", HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Internal server error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
