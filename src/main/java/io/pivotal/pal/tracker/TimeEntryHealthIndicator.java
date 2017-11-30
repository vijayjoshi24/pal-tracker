package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {
    /**
     * Return an indication of health.
     *
     * @return the health for
     */

    private  TimeEntryRepository timeEntryRepository;

    private static final  Integer MAX_LIMIT_DATABASE_ENTRIES = 5;

    public TimeEntryHealthIndicator(TimeEntryRepository timeEntryRepository){

        this.timeEntryRepository = timeEntryRepository;

    }
    @Override
    public Health health() {

        if (timeEntryRepository.list() !=null && timeEntryRepository.list().size() >= MAX_LIMIT_DATABASE_ENTRIES) {
            return Health.down().withDetail("Error Code", "Size Exceeds").build();
        }
        return Health.up().build();
    }
}
