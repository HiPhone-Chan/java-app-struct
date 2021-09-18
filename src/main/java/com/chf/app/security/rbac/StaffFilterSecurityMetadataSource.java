package com.chf.app.security.rbac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import com.chf.app.repository.NavigationRepository;
import com.chf.app.repository.NavigationRoleRepository;

public class StaffFilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private FilterInvocationSecurityMetadataSource srcMetadataSource;

    @Autowired
    private NavigationRoleRepository navigationRoleRepository;

    public StaffFilterSecurityMetadataSource(FilterInvocationSecurityMetadataSource srcMetadataSource) {
        super();
        this.srcMetadataSource = srcMetadataSource;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (object instanceof FilterInvocation) {
            FilterInvocation fi = (FilterInvocation) object;
            String url = fi.getRequestUrl();
            List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();

        }
        
        return srcMetadataSource.getAttributes(object);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return srcMetadataSource.getAllConfigAttributes();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
