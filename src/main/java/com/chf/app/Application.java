package com.chf.app;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import com.chf.app.config.ConfigProperties;
import com.chf.app.constants.SystemConstants;
import com.chf.app.utils.ProfileUtil;

@SpringBootApplication
@EnableConfigurationProperties({ ConfigProperties.class })
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private final Environment env;

    public Application(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void initApplication() throws Exception {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(SystemConstants.PROFILE_DEVELOPMENT)
                && activeProfiles.contains(SystemConstants.PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run "
                    + "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(SystemConstants.PROFILE_DEVELOPMENT)
                && activeProfiles.contains(SystemConstants.PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not "
                    + "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(Application.class);
        ProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isEmpty(contextPath)) {
            contextPath = "/";
        }

        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t" + "Local: \t\t{}://localhost:{}{}\n\t"
                        + "External: \t{}://{}:{}{}\n\t"
                        + "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"), protocol, serverPort, contextPath, protocol, hostAddress,
                serverPort, contextPath, env.getActiveProfiles());
    }
}
