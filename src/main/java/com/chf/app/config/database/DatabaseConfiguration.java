package com.chf.app.config.database;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.chf.app.constants.SystemConstants;

@Configuration
@EnableJpaRepositories(basePackages = SystemConstants.BASE_PACKAGE + ".repository")
@EntityScan({ SystemConstants.BASE_PACKAGE, "org.springframework.data.jpa.convert.threeten" })
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {

}
