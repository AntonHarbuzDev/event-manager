package school.sorokin.event_manager.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sorokin.event_manager.model.Event;
import school.sorokin.event_manager.model.Location;
import school.sorokin.event_manager.model.Status;
import school.sorokin.event_manager.model.User;
import school.sorokin.event_manager.model.dto.EventCreateDto;
import school.sorokin.event_manager.model.dto.EventShowDto;
import school.sorokin.event_manager.model.entity.EventEntity;
import school.sorokin.event_manager.repository.EventRepository;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final UserService userService;

    @Transactional
    public EventShowDto createEvent(EventCreateDto dto, String username) {
        Event event = toBusinessEntity(dto);
        checkCapacity(event);
        User user = userService.getUserByLogin(username);
        event.setOwner(user);
        event.setStatus(Status.WAIT_START);
        event.setEventRegistrations(new HashSet<>());
        EventEntity eventEntity = toEntity(event);
        EventEntity result = eventRepository.save(eventEntity);
        log.info("Create event success {}", result);
        return toShowDto(result);
    }

    private void checkCapacity(Event event) {
        if (event.getLocation().getCapacity() < event.getMaxPlaces()) {
            throw new IllegalArgumentException(String.format("The location = %s cannot accommodate %d people.",
                    event.getLocation().getName(), event.getDuration()));
        }
    }

    private EventEntity toEntity(Event event) {
        return EventEntity.builder()
                .name(event.getName())
                .owner(userService.toEntity(event.getOwner()))
                .date(event.getDate())
                .cost(event.getCost())
                .duration(event.getDuration())
                .locationEntity(locationService.toEntity(event.getLocation()))
                .maxPlaces(event.getMaxPlaces())
                .status(event.getStatus())
                .build();
    }

    private Event toBusinessEntity(EventCreateDto dto) {
        Location location = locationService.getLocationById(dto.getLocationId());
        return Event.builder()
                .name(dto.getName())
                .date(dto.getDate())
                .cost(dto.getCost())
                .duration(dto.getDuration())
                .location(location)
                .maxPlaces(dto.getMaxPlaces())
                .build();
    }

    private EventShowDto toShowDto(EventEntity eventEntity) {
        return EventShowDto.builder()
                .id(eventEntity.getId())
                .ownerId(eventEntity.getOwner().getId())
                .name(eventEntity.getName())
                .locationId(eventEntity.getLocationEntity().getId())
                .occupiedPlaces(eventEntity.getEventRegistrationEntities() == null ? 0 :
                        eventEntity.getEventRegistrationEntities().size())
                .date(eventEntity.getDate())
                .duration(eventEntity.getDuration())
                .cost(eventEntity.getCost())
                .maxPlaces(eventEntity.getMaxPlaces())
                .status(eventEntity.getStatus())
                .build();
    }
}
