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
        // SUPPRIMEZ TOUS LES System.out.println() - ils consomment la mémoire !
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://postgres.railway.internal:5432/railway")
                .username("postgres")
                .password(System.getenv("PGPASSWORD"))
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
        vendorAdapter.setShowSql(false); // ← IMPORTANT: désactiver
        em.setJpaVendorAdapter(vendorAdapter);

        Properties props = new Properties();
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "false"); // ← IMPORTANT: désactiver
        props.put("hibernate.format_sql", "false");
        em.setJpaProperties(props);

        return em;
    }

    @Bean
    @Primary
    public JpaTransactionManager transactionManager(jakarta.persistence.EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}