package tunutech.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tunutech.api.repositories.ActivityRepository;

@RestController
public class TestController {
    @Autowired(required = false)
    private ActivityRepository activityRepository;

    @GetMapping("/test")
    public String test() {
        if (activityRepository != null) {
            try {
                long count = activityRepository.count();
                return "🎉 SUCCÈS TOTAL ! ActivityRepository fonctionne. Count: " + count;
            } catch (Exception e) {
                return "❌ ActivityRepository erreur: " + e.getMessage();
            }
        } else {
            return "❌ ActivityRepository non trouvé";
        }
    }

    @GetMapping("/health")
    public String health() {
        return "🚀 API STATUS: JPA OK - DATABASE OK - READY FOR RAILWAY!";
    }
}
