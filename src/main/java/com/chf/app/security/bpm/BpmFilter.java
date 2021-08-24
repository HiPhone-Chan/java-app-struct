package com.chf.app.security.bpm;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.web.filter.GenericFilterBean;

import com.chf.app.security.SecurityUtils;

public class BpmFilter extends GenericFilterBean {

    private IdentityService identityService;

    public BpmFilter(IdentityService identityService) {
        super();
        this.identityService = identityService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        SecurityUtils.getCurrentUserLogin().ifPresent(login -> {
            identityService.setAuthenticatedUserId(login);
        });
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
