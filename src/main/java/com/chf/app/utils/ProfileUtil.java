package com.chf.app.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;

import com.chf.app.constants.SystemConstants;

public final class ProfileUtil {

    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

    private ProfileUtil() {
    }

    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        defProperties.put(SPRING_PROFILE_DEFAULT, SystemConstants.PROFILE_DEVELOPMENT);
        app.setDefaultProperties(defProperties);
    }

}
