package school.sorokin.event_manager.service.sender;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import school.sorokin.event_manager.model.EventKafka;

@Component
@RequiredArgsConstructor
public class EventSender {

    private static final Logger log = LoggerFactory.getLogger(EventSender.class);

    private final KafkaTemplate<Long, EventKafka> kafkaTemplate;

    public void sendEvent(EventKafka eventKafka) {
        log.info("Sending event = {}", eventKafka);

        var result = kafkaTemplate.send(
                "event",
                eventKafka.getEventId(),
                eventKafka
        );

        result.whenComplete((sendResult, throwable) -> {
            if (throwable != null) {
                log.error("Failed send event id - {} to kafka: {}", eventKafka.getEventId(), throwable.getMessage());
            } else {
                log.info("Event id - {} send to kafka success", eventKafka.getEventId());
            }
        });
    }
}
