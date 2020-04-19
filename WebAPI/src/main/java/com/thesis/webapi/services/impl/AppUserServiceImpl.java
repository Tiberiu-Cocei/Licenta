package com.thesis.webapi.services.impl;

import com.thesis.webapi.dtos.AppUserCreateDto;
import com.thesis.webapi.entities.AppUser;
import com.thesis.webapi.security.PasswordHashing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import com.thesis.webapi.repositories.AppUserRepository;
import com.thesis.webapi.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UUID getToken(String username, String password) {
        AppUser appUser = appUserRepository.login(username,password);
        if(appUser != null){
            UUID authenticationToken = UUID.randomUUID();
            appUser.setAuthenticationToken(authenticationToken);
            appUserRepository.save(appUser);
            return authenticationToken;
        }
        return null;
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
    public String getSalt(String username) {
        return appUserRepository.getSalt(username);
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

}
