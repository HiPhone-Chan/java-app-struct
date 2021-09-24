package com.chf.app.security.rbac;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import com.chf.app.constants.StaffConstants;
import com.chf.app.service.RoleService;

public class StaffVoter implements AccessDecisionVoter<FilterInvocation> {

    private RoleService roleService;

    public StaffVoter(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation,
            Collection<ConfigAttribute> attributes) {
        if (authentication == null) {
            return ACCESS_DENIED;
        }

        HttpServletRequest request = filterInvocation.getHttpRequest();
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        String path = requestURI;
        if (StringUtils.isNotEmpty(contextPath) && !contextPath.equals("/")) {
            path = path.substring(contextPath.length());
        }

        if (StringUtils.isEmpty(path) || !path.startsWith(StaffConstants.API_PREFIX)) {
            return ACCESS_ABSTAIN;
        }
        String method = filterInvocation.getRequest().getMethod().toLowerCase();

        if (roleService.hasCurrentUserThisAuthority(method, path)) {
            return ACCESS_GRANTED;
        }
        return ACCESS_DENIED;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
