package school.sorokin.event_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sorokin.event_manager.model.Status;
import school.sorokin.event_manager.model.entity.EventEntity;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventStatusService {

    private final EventService eventService;

    @Transactional
    public void updateAllEventStatuses() {
        OffsetDateTime now = OffsetDateTime.now();
        updateToStarted(now);
        updateToFinished(now);
    }

    private void updateToStarted(OffsetDateTime now) {
        List<EventEntity> events = eventService.getEventsForChangeStatus(Status.WAIT_START, now);
        events.forEach(event -> eventService.updateEventStatus(event, Status.STARTED));
    }

    private void updateToFinished(OffsetDateTime now) {
        List<EventEntity> events = eventService.getEventsForChangeStatus(Status.STARTED, now);
        List<EventEntity> eventsToFinish = events.stream()
                .filter(event -> isEventFinished(event, now))
                .toList();
        eventsToFinish.forEach(event -> eventService.updateEventStatus(event, Status.FINISHED));
    }

    private boolean isEventFinished(EventEntity event, OffsetDateTime now) {
        return event.getDate().plusMinutes(event.getDuration()).isBefore(now);
    }
}
