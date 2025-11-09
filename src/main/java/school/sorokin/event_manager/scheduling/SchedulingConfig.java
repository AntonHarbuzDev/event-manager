package school.sorokin.event_manager.scheduling;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import school.sorokin.event_manager.service.EventStatusService;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class SchedulingConfig {

    private static final Logger log = LoggerFactory.getLogger(SchedulingConfig.class);

    private final EventStatusService eventStatusService;

    @Scheduled(fixedDelayString = "PT01M")
    public void updateEventStatuses() {
        log.info("Method updateEventStatuses begin");
        eventStatusService.updateAllEventStatuses();
        log.info("Method updateEventStatuses finish");
    }
}
