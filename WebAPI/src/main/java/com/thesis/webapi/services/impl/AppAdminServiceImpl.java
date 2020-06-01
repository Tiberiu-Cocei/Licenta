package com.thesis.webapi.services.impl;

import com.thesis.webapi.dtos.AppAdminCreateDto;
import com.thesis.webapi.dtos.AppAdminLoggedInDto;
import com.thesis.webapi.entities.AppAdmin;
import com.thesis.webapi.repositories.AppAdminRepository;
import com.thesis.webapi.security.PasswordHashing;
import com.thesis.webapi.services.AppAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AppAdminServiceImpl implements AppAdminService {

    private final AppAdminRepository appAdminRepository;

    @Autowired
    public AppAdminServiceImpl(AppAdminRepository appAdminRepository) {
        this.appAdminRepository = appAdminRepository;
    }

    @Override
    public ResponseEntity<AppAdminLoggedInDto> login(String username, String password, PasswordHashing passwordHashing) {
        try {
            username = username.toLowerCase();
            String salt = appAdminRepository.getSalt(username);
            AppAdmin appAdmin = null;
            if(salt != null) {
                appAdmin = appAdminRepository.login(username, passwordHashing.hashString(password, salt));
            }
            if (appAdmin != null) {
                UUID authenticationToken = UUID.randomUUID();
                appAdmin.setAuthenticationToken(authenticationToken);
                appAdminRepository.save(appAdmin);
                AppAdminLoggedInDto appAdminLoggedInDto = new AppAdminLoggedInDto(appAdmin);
                return new ResponseEntity<>(appAdminLoggedInDto, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<User> findAdminByAuthenticationToken(String authenticationToken) {
        Optional<AppAdmin> appAdminOpt = appAdminRepository.findAdminByAuthenticationToken(UUID.fromString(authenticationToken));
        if(appAdminOpt.isPresent()){
            AppAdmin appAdmin = appAdminOpt.get();
            User admin = new User(appAdmin.getUsername(), appAdmin.getPassword(), true, true,
                    true, true, AuthorityUtils.createAuthorityList("ADMIN"));
            return Optional.of(admin);
        }
        return Optional.empty();
    }

    @Override
    public ResponseEntity<String> saveAppAdmin(AppAdminCreateDto appAdminCreateDto, PasswordHashing passwordHashing) {
        appAdminCreateDto.setUsername(appAdminCreateDto.getUsername().toLowerCase());
        if(appAdminRepository.isUsernameTaken(appAdminCreateDto.getUsername()) != null) {
            return new ResponseEntity<>("Username is already in use.", HttpStatus.OK);
        }
        AppAdmin appAdmin = new AppAdmin(appAdminCreateDto);
        appAdmin.setSalt(passwordHashing.generateSalt());
        try {
            appAdmin.setPassword(passwordHashing.hashString(appAdmin.getPassword(), appAdmin.getSalt()));
            appAdminRepository.save(appAdmin);
            return new ResponseEntity<>("Successfully created admin account.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create new admin account.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
