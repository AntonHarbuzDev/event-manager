package school.sorokin.event_manager.scheduling;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import school.sorokin.event_manager.model.Status;
import school.sorokin.event_manager.model.entity.EventEntity;
import school.sorokin.event_manager.service.EventService;

import java.time.OffsetDateTime;
import java.util.List;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class SchedulingConfig {

    private static final Logger log = LoggerFactory.getLogger(SchedulingConfig.class);

    private final EventService eventService;

    @Scheduled(fixedDelayString = "PT01M")
    public void updateStatusToStarted() {
        log.info("Method updateStatusToStarted begin");
        List<EventEntity> eventEntities = eventService.getEventsForChangeStatus(Status.WAIT_START, OffsetDateTime.now());
        updateStatus(eventEntities, Status.STARTED);
    }

    @Scheduled(fixedDelayString = "PT01M")
    public void updateStatusToFinish() {
        log.info("Method updateStatusToFinish begin");
        OffsetDateTime dateTime = OffsetDateTime.now();
        List<EventEntity> eventEntities = eventService.getEventsForChangeStatus(Status.STARTED, dateTime);

        List<EventEntity> result =
                eventEntities.stream().filter(e -> e.getDate().plusMinutes(e.getDuration()).isBefore(dateTime)).toList();
        updateStatus(result, Status.FINISHED);
    }

    private void updateStatus(List<EventEntity> eventEntities, Status status) {
        if (eventEntities.isEmpty()) {
            log.info("There are no events to change the status to {}...", status);
        } else {
            eventEntities.forEach(e -> eventService.updateEventStatus(e, status));
        }
    }
}
