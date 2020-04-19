package com.thesis.webapi.controllers;

import com.thesis.webapi.dtos.AppUserCreateDto;
import com.thesis.webapi.security.PasswordHashing;
import com.thesis.webapi.services.AppUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api")
public class AppUserController {

    private final AppUserService appUserService;

    private final PasswordHashing passwordHashing;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
        this.passwordHashing = new PasswordHashing();
    }

    @PostMapping(value = "/unsecure/users/register")
    public ResponseEntity<String> register(@Valid @RequestBody AppUserCreateDto appUserCreateDto) {
        return appUserService.saveAppUser(appUserCreateDto, passwordHashing);
    }

    @PostMapping(value = "/unsecure/users/token")
    public String getToken(@RequestParam("username") final String username, @RequestParam("password") final String password){
        try {
            String salt = appUserService.getSalt(username);
            String token = appUserService.getToken(username, passwordHashing.hashString(password, salt)).toString();
            if (StringUtils.isEmpty(token)) {
                return "no token found";
            }
            return token;
        } catch (Exception e) {
            return "no token found";
        }
    }

}
