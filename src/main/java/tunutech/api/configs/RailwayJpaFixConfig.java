package tunutech.api.configs;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("nosecurity")
public class RailwayJpaFixConfig {

    @Bean
    @Profile("nosecurity")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            EntityManagerFactoryBuilder builder) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.jdbc.lob.non_contextual_creation", "true");
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");

        // IMPORTANT: Configuration minimale pour Railway
        properties.put("hibernate.enhancer.enableDirtyTracking", "false");
        properties.put("hibernate.enhancer.enableLazyInitialization", "false");
        properties.put("hibernate.enhancer.enableAssociationManagement", "false");

        return builder
                .dataSource(dataSource)
                .packages("tunutech.api.model")
                .persistenceUnit("default")
                .properties(properties)
                .build();
    }

    @Bean
    @Profile("nosecurity")
    public JpaTransactionManager transactionManager(
            jakarta.persistence.EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}