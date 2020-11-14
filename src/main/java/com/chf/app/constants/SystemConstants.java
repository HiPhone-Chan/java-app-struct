package com.chf.app.constants;

import com.chf.app.Application;

public interface SystemConstants {

    // String BASE_PACKAGE_NAME = Application.class.getPackage().getName();
    String BASE_PACKAGE_NAME = "com.chf.app";

    String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    String SYSTEM_ACCOUNT = "system";

    String ANONYMOUS_USER = "anonymoususer";

    String LANG = "zh_cn";

    // profile
    String PROFILE_PRODUCTION = "prod";

    String PROFILE_DEVELOPMENT = "dev";

    String PROFILE_CLOUD = "cloud";

}
