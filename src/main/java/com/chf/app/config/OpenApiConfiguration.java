package com.chf.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.chf.app.config.ConfigProperties.Swagger;
import com.chf.app.constants.SystemConstants;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableOpenApi
@Configuration
@Profile(SystemConstants.PROFILE_DEVELOPMENT)
public class OpenApiConfiguration {

    private final ConfigProperties configProperties;

    public OpenApiConfiguration(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(SystemConstants.BASE_PACKAGE_NAME))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        Swagger swagger = configProperties.getSwagger();
        return new ApiInfoBuilder()
                .title(swagger.getTitle())
                .description(swagger.getDescription())
                .contact(swagger.getContact())
                .version(swagger.getVersion())
                .build();
    }
}
