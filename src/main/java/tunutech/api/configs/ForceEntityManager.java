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
import java.util.Map;
import java.util.Properties;

@Configuration
@Profile("nosecurity")
public class ForceEntityManager {

    @Bean
    @Primary
    public DataSource dataSource() {
        System.out.println("=== VÉRIFICATION DES VARIABLES (PostgreSQL ACTIF) ===");

        // Afficher toutes les variables pour debug
        Map<String, String> env = System.getenv();
        env.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });

        // Essayer les variables Railway standard
        String host = System.getenv("PGHOST");
        String port = System.getenv("PGPORT");
        String database = System.getenv("PGDATABASE");
        String username = System.getenv("PGUSER");
        String password = System.getenv("PGPASSWORD");

        if (host != null) {
            System.out.println("✅ Variables PostgreSQL trouvées !");
            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, database);
            System.out.println("URL: " + jdbcUrl);

            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        } else {
            throw new IllegalStateException("""
                ❌ Variables PostgreSQL toujours pas trouvées !
                
                Sur Railway, allez dans :
                1. Votre service d'application (Spring Boot)
                2. Onglet "Variables" 
                3. Vérifiez que les variables PostgreSQL sont injectées
                """);
        }
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
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        em.setJpaProperties(props);

        return em;
    }

    @Bean
    @Primary
    public JpaTransactionManager transactionManager(jakarta.persistence.EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}