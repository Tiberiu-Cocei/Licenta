package com.thesis.webapi.controllers;

import com.thesis.webapi.dtos.AppAdminLoggedInDto;
import com.thesis.webapi.dtos.AppAdminLoginDto;
import com.thesis.webapi.security.PasswordHashing;
import com.thesis.webapi.services.AppAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api")
public class AppAdminController {

    private final PasswordHashing passwordHashing;

    private final AppAdminService appAdminService;

    @Autowired
    public AppAdminController(AppAdminService appAdminService) {
        this.appAdminService = appAdminService;
        this.passwordHashing = new PasswordHashing();
    }

//    @PostMapping(value = "/unsecure/admins/register")
//    public ResponseEntity<String> register(@Valid @RequestBody AppAdminCreateDto appAdminCreateDto) {
//        return appAdminService.saveAppAdmin(appAdminCreateDto, passwordHashing);
//    }

    @PostMapping(value = "/unsecure/admins/login")
    public ResponseEntity<AppAdminLoggedInDto> login(@Valid @RequestBody AppAdminLoginDto appAdminLoginDto) {
        return appAdminService.login(appAdminLoginDto.getUsername(), appAdminLoginDto.getPassword(), passwordHashing);
    }

}
