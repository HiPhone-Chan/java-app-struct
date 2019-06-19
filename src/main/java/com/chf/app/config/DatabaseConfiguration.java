package com.chf.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.chf.app.repository.support.JpaExtRepositoryFactoryBean;

@Configuration
@EnableJpaRepositories(basePackages = "com.chf.app.repository", repositoryFactoryBeanClass = JpaExtRepositoryFactoryBean.class)
@EntityScan({ "com.chf.app", "org.springframework.data.jpa.convert.threeten" })
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {

}
