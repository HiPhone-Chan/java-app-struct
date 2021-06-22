package com.chf.app.config.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "multi")
public class MultiProperties {

    private final Map<String, Multi> properties = new HashMap<>();

    public Map<String, Multi> getProperties() {
        return properties;
    }

    public static class Multi {

        private final DataSourceProperties dataSourceProperties = new DataSourceProperties();
        private final JpaProperties jpaProperties = new JpaProperties();

        public DataSourceProperties getDataSourceProperties() {
            return dataSourceProperties;
        }

        public JpaProperties getJpaProperties() {
            return jpaProperties;
        }

    }

}
