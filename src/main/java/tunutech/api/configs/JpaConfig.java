package tunutech.api.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("nosecurity")
@EnableJpaRepositories(
        basePackages = "tunutech.api.repositories",
        entityManagerFactoryRef = "entityManagerFactory"  // ← Utilise le nom STANDARD
)
public class JpaConfig {
    // Rien d'autre - Spring Boot fera tout automatiquement
}