package school.sorokin.event_manager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sorokin.event_manager.model.EventChangeFields;
import school.sorokin.event_manager.model.EventKafka;
import school.sorokin.event_manager.model.entity.EventNotificationEntity;
import school.sorokin.event_manager.repository.EventNotificationRepository;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class EventNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EventNotificationService.class);

    private final ObjectMapper objectMapper;
    private final EventNotificationRepository eventNotificationRepository;

    @Transactional
    public void create(EventKafka eventKafka) {
        eventKafka.getRegistrationUserIds().forEach(
                id -> log.info("Notification created success {}",
                        eventNotificationRepository.save(toEntity(eventKafka, id)))
        );
    }

    private EventNotificationEntity toEntity(EventKafka eventKafka, Long ownerId) {
        return EventNotificationEntity.builder()
                .eventId(eventKafka.getEventId())
                .ownerNotificationId(ownerId)
                .userChangedId(eventKafka.getUserChangedId() == null ? null : eventKafka.getUserChangedId())
                .dateCreated(OffsetDateTime.now())
                .isRead(false)
                .fieldsChangeJson(serializeChanges(eventKafka.getEventChangeFields()))
                .build();
    }

    private String serializeChanges(EventChangeFields changes) {
        try {
            return objectMapper.writeValueAsString(changes);
        } catch (Exception e) {
            return "{}";
        }
    }
}
