package school.sorokin.event_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventRegistration {

    private Long id;
    private User user;
    private Event event;
}
