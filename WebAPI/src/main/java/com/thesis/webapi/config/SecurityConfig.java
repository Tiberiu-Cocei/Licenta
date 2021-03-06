package com.thesis.webapi.config;

import com.thesis.webapi.security.AuthenticationFilter;
import com.thesis.webapi.security.AuthenticationProviderAdmin;
import com.thesis.webapi.security.AuthenticationProviderUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationProviderUser authenticationProviderUser;

    private AuthenticationProviderAdmin authenticationProviderAdmin;

    private static final RequestMatcher PROTECTED_URLS
            = new OrRequestMatcher(new AntPathRequestMatcher("/api/secure/**"),
                                   new AntPathRequestMatcher("/api/admin/**"));

    private static final RequestMatcher PROTECTED_URLS_USER
            = new OrRequestMatcher(new AntPathRequestMatcher("/api/secure/**"));

    private static final RequestMatcher PROTECTED_URLS_ADMIN
            = new OrRequestMatcher(new AntPathRequestMatcher("/api/admin/**"));

    @Autowired
    public SecurityConfig(AuthenticationProviderUser authenticationProvider,
                          AuthenticationProviderAdmin authenticationProviderAdmin) {
        super();
        this.authenticationProviderUser = authenticationProvider;
        this.authenticationProviderAdmin = authenticationProviderAdmin;
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(authenticationProviderUser);
        authenticationManagerBuilder.authenticationProvider(authenticationProviderAdmin);
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/api/unsecure/**");
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .exceptionHandling()
                    .and()
                    .authenticationProvider(authenticationProviderUser)
                    .authenticationProvider(authenticationProviderAdmin)
                    .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
                    .authorizeRequests()
                    .requestMatchers(PROTECTED_URLS_USER)
                    .hasAuthority("USER")
                    .and()
                    .authorizeRequests()
                    .requestMatchers(PROTECTED_URLS_ADMIN)
                    .hasAuthority("ADMIN")
                    .and()
                    .csrf().disable()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .logout().disable();
                    //.requiresChannel()
                    //.anyRequest()
                    //.requiresSecure();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
    }

}
