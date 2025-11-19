package tunutech.api.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.support.Repositories;
import tunutech.api.repositories.ActivityRepository;

@Configuration
public class DiagnosticConfig {

    @Bean
    public CommandLineRunner diagnosticRunner(ApplicationContext context) {
        return args -> {
            System.out.println("=== SPRING DIAGNOSTIC ===");

            // Vérifier les repositories
            try {
                String[] repoBeans = context.getBeanNamesForType(org.springframework.data.repository.Repository.class);
                System.out.println("Repositories trouvés: " + repoBeans.length);
                for (String bean : repoBeans) {
                    System.out.println(" - " + bean);
                }
            } catch (Exception e) {
                System.out.println("Erreur repositories: " + e.getMessage());
            }

            // Vérifier ActivityRepository spécifiquement
            try {
                ActivityRepository repo = context.getBean(ActivityRepository.class);
                System.out.println("✅ ActivityRepository: TROUVÉ");
            } catch (Exception e) {
                System.out.println("❌ ActivityRepository: NON TROUVÉ - " + e.getMessage());
            }

            // Vérifier les entités
            try {
                String[] entities = context.getBeanNamesForType(jakarta.persistence.Entity.class);
                System.out.println("Entités JPA trouvées: " + entities.length);
            } catch (Exception e) {
                System.out.println("Erreur entités: " + e.getMessage());
            }
        };
    }
}