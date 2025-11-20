package tunutech.api.configs;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Profile("nosecurity")
public class ForceEntityManager {

    @Bean
    @Primary
    public DataSource dataSource() {
        // DEBUG: Afficher toutes les variables d'environnement
        System.out.println("=== VARIABLES D'ENVIRONNEMENT RAILWAY ===");
        System.out.println("PGHOST: " + System.getenv("PGHOST"));
        System.out.println("PGPORT: " + System.getenv("PGPORT"));
        System.out.println("PGDATABASE: " + System.getenv("PGDATABASE"));
        System.out.println("PGUSER: " + System.getenv("PGUSER"));
        System.out.println("PGPASSWORD: " + (System.getenv("PGPASSWORD") != null ? "***" : "null"));
        System.out.println("DATABASE_URL: " + System.getenv("DATABASE_URL"));

        // Chercher aussi les variables Railway spécifiques
        System.out.println("RAILWAY_ENVIRONMENT: " + System.getenv("RAILWAY_ENVIRONMENT"));

        String host = System.getenv("PGHOST");
        String port = System.getenv("PGPORT");
        String database = System.getenv("PGDATABASE");
        String username = System.getenv("PGUSER");
        String password = System.getenv("PGPASSWORD");

        if (host == null) {
            throw new IllegalStateException("❌ VARIABLES POSTGRESQL NON TROUVÉES sur Railway!");
        }

        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, database);
        System.out.println("✅ URL JDBC: " + jdbcUrl);

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "jpaSharedEM_entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("tunutech.api.model");
        em.setPersistenceUnitName("jpaSharedEM_entityManagerFactory");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        em.setJpaVendorAdapter(vendorAdapter);

        Properties props = new Properties();
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        em.setJpaProperties(props);

        return em;
    }

    @Bean
    @Primary
    public JpaTransactionManager transactionManager(jakarta.persistence.EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}