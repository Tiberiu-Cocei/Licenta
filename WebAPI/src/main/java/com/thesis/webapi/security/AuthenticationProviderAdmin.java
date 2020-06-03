package com.thesis.webapi.security;

import com.thesis.webapi.services.AppAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationProviderAdmin extends AbstractUserDetailsAuthenticationProvider {

    private final AppAdminService appAdminService;

    @Autowired
    public AuthenticationProviderAdmin(AppAdminService appAdminService) {
        this.appAdminService = appAdminService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {}

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        Object token = usernamePasswordAuthenticationToken.getCredentials();
        return Optional
                .ofNullable(token)
                .map(String::valueOf)
                .flatMap(appAdminService::findAdminByAuthenticationToken)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find admin with authentication token = " + token));
    }

}