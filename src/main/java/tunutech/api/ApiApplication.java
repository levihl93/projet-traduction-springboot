package tunutech.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
		basePackages = "tunutech.api.repositories",
		enableDefaultTransactions = false  // ← IMPORTANT pour Railway
)
@EntityScan(basePackages = "tunutech.api.model")  // ← CRITIQUE
public class ApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
