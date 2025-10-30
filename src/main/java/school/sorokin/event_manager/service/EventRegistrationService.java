package school.sorokin.event_manager.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import school.sorokin.event_manager.model.Event;
import school.sorokin.event_manager.model.EventRegistration;
import school.sorokin.event_manager.model.User;
import school.sorokin.event_manager.model.entity.EventRegistrationEntity;
import school.sorokin.event_manager.repository.EventRegistrationRepository;

@Service
@RequiredArgsConstructor
public class EventRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(EventRegistrationService.class);

    private final EventRegistrationRepository eventRegistrationRepository;
    private final UserService userService;
    private final EventService eventService;



//    public EventRegistrationE toBusinessEntity(EventRegistrationEntity entity) {
//        User user = userService.toBusinessEntity(entity.getUserEntity());
//        Event event = eventService.toBusinessEntity(entity.getEventEntity());
//        return new EventRegistration(
//                entity.getId(),
//                user,
//                event
//        );
//    }
}
