package com.thesis.webapi.services;

import com.thesis.webapi.dtos.AppAdminCreateDto;
import com.thesis.webapi.dtos.AppAdminLoggedInDto;
import com.thesis.webapi.security.PasswordHashing;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface AppAdminService {

    ResponseEntity<AppAdminLoggedInDto> login(String username, String password, PasswordHashing passwordHashing);

    Optional<User> findAdminByAuthenticationToken(String authenticationToken);

    ResponseEntity<String> saveAppAdmin(AppAdminCreateDto appAdminCreateDto, PasswordHashing passwordHashing);

}
