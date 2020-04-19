package com.thesis.webapi.services;

import com.thesis.webapi.dtos.AppUserCreateDto;
import com.thesis.webapi.security.PasswordHashing;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;
import java.util.UUID;

public interface AppUserService {

    UUID getToken(String username, String password);

    Optional<User> findUserByAuthenticationToken(String authenticationToken);

    String getSalt(String username);

    ResponseEntity<String> saveAppUser(AppUserCreateDto appUserCreateDto, PasswordHashing passwordHashing);

}
