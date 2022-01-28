package com.chf.app.config.apidoc;

import static org.springdoc.core.Constants.DEFAULT_GROUP_NAME;
import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;
import static org.springdoc.core.SpringDocUtils.getConfig;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.GroupedOpenApi.Builder;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.ActuatorOpenApiCustomizer;
import org.springdoc.core.customizers.ActuatorOperationCustomizer;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chf.app.config.apidoc.customizer.MyOpenApiCustomizer;
import com.chf.app.config.properties.ConfigProperties;

import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SpringDocGroupsConfiguration {

    public static final String MANAGEMENT_GROUP_NAME = "management";

    static {
        getConfig().replaceWithClass(ByteBuffer.class, String.class);
    }

    static final String MANAGEMENT_TITLE_SUFFIX = "Management API";
    static final String MANAGEMENT_DESCRIPTION = "Management endpoints documentation";

    private final Logger log = LoggerFactory.getLogger(SpringDocGroupsConfiguration.class);

    private final ConfigProperties.ApiDocs properties;

    /**
     * <p>Constructor for OpenApiAutoConfiguration.</p>
     *
     * @param jHipsterProperties a {@link JHipsterProperties} object.
     */
    public SpringDocGroupsConfiguration(ConfigProperties configProperties) {
        this.properties = configProperties.getApiDocs();
    }

    /**
     * JHipster OpenApi Customiser
     *
     * @return the Customizer of JHipster
     */
    @Bean
    public MyOpenApiCustomizer jhipsterOpenApiCustomizer() {
        log.debug("Initializing JHipster OpenApi customizer");
        return new MyOpenApiCustomizer(properties);
    }

    /**
     * OpenApi default group configuration.
     *
     * @return the GroupedOpenApi configuration
     */
    @Bean
    @ConditionalOnMissingBean(name = "openAPIDefaultGroupedOpenAPI")
    public GroupedOpenApi openAPIDefaultGroupedOpenAPI(List<OpenApiCustomiser> openApiCustomisers,
            List<OperationCustomizer> operationCustomizers,
            @Qualifier("apiFirstGroupedOpenAPI") Optional<GroupedOpenApi> apiFirstGroupedOpenAPI,
            SpringDocConfigProperties springDocConfigProperties) {
        log.debug("Initializing JHipster OpenApi default group");
        Builder builder = GroupedOpenApi.builder().group(DEFAULT_GROUP_NAME)
                .pathsToMatch(properties.getDefaultIncludePattern());
        openApiCustomisers.stream().filter(customiser -> !(customiser instanceof ActuatorOpenApiCustomizer))
                .forEach(builder::addOpenApiCustomiser);
        operationCustomizers.stream().filter(customiser -> !(customiser instanceof ActuatorOperationCustomizer))
                .forEach(builder::addOperationCustomizer);
        apiFirstGroupedOpenAPI.ifPresent(apiFirst -> {
            if (apiFirst.getPackagesToScan() != null) {
                apiFirst.getPackagesToScan().stream().forEach(builder::packagesToExclude);
            }
        });
        return builder.build();
    }

    /**
     * OpenApi management group configuration for the management endpoints (actuator) OpenAPI docs.
     *
     * @return the GroupedOpenApi configuration
     */
    @Bean
    @ConditionalOnClass(name = "org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties")
    @ConditionalOnMissingBean(name = "openAPIManagementGroupedOpenAPI")
    @ConditionalOnProperty(SPRINGDOC_SHOW_ACTUATOR)
    public GroupedOpenApi openAPIManagementGroupedOpenAPI(
            @Value("${spring.application.name:application}") String appName,
            ActuatorOpenApiCustomizer actuatorOpenApiCustomiser, ActuatorOperationCustomizer actuatorCustomizer) {
        log.debug("Initializing JHipster OpenApi management group");
        return GroupedOpenApi.builder().group(MANAGEMENT_GROUP_NAME).addOpenApiCustomiser(openApi -> {
            openApi.info(new Info().title(StringUtils.capitalize(appName) + " " + MANAGEMENT_TITLE_SUFFIX)
                    .description(MANAGEMENT_DESCRIPTION).version(properties.getVersion()));
        }).addOpenApiCustomiser(actuatorOpenApiCustomiser).addOperationCustomizer(actuatorCustomizer)
                .pathsToMatch(properties.getManagementIncludePattern()).build();
    }
}
