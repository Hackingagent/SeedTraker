package notification;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import models.Seed;
import models.User;
import utils.EmailService;
import utils.SeedManager;

public class SeedExpirationNotifier {
    private ScheduledExecutorService scheduler;

    public SeedExpirationNotifier() {
        scheduler = new ScheduledThreadPoolExecutor(1);
        scheduler.scheduleAtFixedRate(this::checkExpiringSeeds, 0, 1, TimeUnit.DAYS);
    }

    private void checkExpiringSeeds() {
        // Retrieve seeds that are 7 days away from expiring
        SeedManager seedManager = new SeedManager();
        List<Seed> expiringSeeds = seedManager.getExpiringSeeds(7);

        // Send email notifications to the users who added the seeds
        for (Seed seed : expiringSeeds) {
            User user = seed.getAddedBy();
            String email = user.getEmail();
            String subject = "Seed Expiration Notification";
            String message = "The seed you added, " + seed.getSeedType() + ", is expiring in 7 days.";

            // Send the email notification
            EmailService.sendEmail(email, subject, message);
        }
    }
}
