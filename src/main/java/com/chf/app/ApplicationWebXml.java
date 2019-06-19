package com.chf.app;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.chf.app.utils.ProfileUtil;

public class ApplicationWebXml extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        ProfileUtil.addDefaultProfile(application.application());
        return application.sources(Application.class);
    }
}
