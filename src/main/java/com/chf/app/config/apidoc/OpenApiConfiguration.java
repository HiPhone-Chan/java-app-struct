package com.chf.app.config.apidoc;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.chf.app.config.apidoc.customizer.MyOpenApiCustomizer;
import com.chf.app.config.properties.ConfigProperties;
import com.chf.app.constants.SystemConstants;

@Configuration
@Profile(SystemConstants.PROFILE_DEVELOPMENT)
public class OpenApiConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "apiFirstGroupedOpenAPI")
    public GroupedOpenApi apiFirstGroupedOpenAPI(MyOpenApiCustomizer openApiCustomizer,
            ConfigProperties configProperties) {
        ConfigProperties.ApiDocs properties = configProperties.getApiDocs();
        return GroupedOpenApi.builder().group("openapi").addOpenApiCustomiser(openApiCustomizer)
                .packagesToScan(SystemConstants.BASE_PACKAGE).pathsToMatch(properties.getDefaultIncludePattern())
                .build();
    }
}
