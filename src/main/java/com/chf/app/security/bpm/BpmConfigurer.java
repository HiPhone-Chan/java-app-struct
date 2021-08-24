package com.chf.app.security.bpm;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

import com.chf.app.security.jwt.JWTFilter;

public class BpmConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private IdentityService identityService;

    public BpmConfigurer(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        BpmFilter filter = new BpmFilter(identityService);
        http.addFilterBefore(filter, JWTFilter.class);
    }
}
