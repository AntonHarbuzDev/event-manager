package school.sorokin.event_manager.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sorokin.event_manager.model.Event;
import school.sorokin.event_manager.model.EventRegistration;
import school.sorokin.event_manager.model.Location;
import school.sorokin.event_manager.model.Status;
import school.sorokin.event_manager.model.User;
import school.sorokin.event_manager.model.dto.EventCreateDto;
import school.sorokin.event_manager.model.dto.EventShowDto;
import school.sorokin.event_manager.model.entity.EventEntity;
import school.sorokin.event_manager.model.entity.LocationEntity;
import school.sorokin.event_manager.model.filter.EventFilter;
import school.sorokin.event_manager.repository.EventRepository;
import school.sorokin.event_manager.service.specification.EventSpecifications;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final UserService userService;
    private final EventSpecifications eventSpecifications;

    @Transactional
    public EventShowDto createEvent(EventCreateDto dto, String username) {
        Event event = toBusinessEntity(dto);
        checkCapacityLocation(event);
        User user = userService.getUserByLogin(username);
        event.setOwner(user);
        event.setStatus(Status.WAIT_START);
        event.setEventRegistrations(new HashSet<>());
        EventEntity eventEntity = toEntity(event);
        EventEntity result = eventRepository.save(eventEntity);
        log.info("Create event success {}", result);
        return toShowDto(result);
    }

    @Transactional(readOnly = true)
    public EventShowDto getEventShowDtoById(Long id) {
        EventEntity eventEntity = loadEventEntityById(id);
        EventShowDto result = toShowDto(eventEntity);
        log.info("Get event = {}", result);
        return result;
    }

    @Transactional
    public List<EventShowDto> getByFilter(EventFilter eventFilter) {
        Specification<EventEntity> spec = eventSpecifications.withFilter(eventFilter);
        List<EventEntity> eventEntities = eventRepository.findAll(spec);
        if (eventEntities.isEmpty()) {
            throw new NoSuchElementException("The entity was not found with the given filter parameters.");
        }
        return eventEntities.stream().map(this::toShowDto).toList();
    }

    @Transactional
    public EventShowDto updateEvent(EventCreateDto dto, Long id) {
        Event event = toBusinessEntity(dto);
        checkCapacityLocation(event);
        EventEntity loadedEventEntity = loadEventEntityById(id);
        User userLogin = getUserFromAuthentication();
        Event eventLoaded = toBusinessEntity(loadedEventEntity);
        checkOwer(eventLoaded, userLogin);
        EventEntity updated = updatedFields(loadedEventEntity, event);
        EventEntity result = eventRepository.save(updated);
        log.info("Update event success {}", result);
        return toShowDto(result);
    }

    @Transactional
    public void cancelEvent(Long id) {
        EventEntity loaded = loadEventEntityById(id);
        checkOwer(toBusinessEntity(loaded), getUserFromAuthentication());
        loaded.setStatus(Status.CANCELLED);
        log.info("Cancel event {} success", loaded);
    }

    private void checkCapacityLocation(Event event) {
        if (event.getLocation().getCapacity() < event.getMaxPlaces()) {
            throw new IllegalArgumentException(String.format("The location = %s cannot accommodate %d people.",
                    event.getLocation().getName(), event.getDuration()));
        }
    }

    private void checkOwer(Event event, User userLogin) {
        if (!event.getOwner().equals(userLogin)) {
            throw new AuthorizationDeniedException(String.format(
                    "User login = %s no owner from event = %s in owner = %s",
                    userLogin, event.getName(), event.getOwner()));
        }
    }

    private EventEntity loadEventEntityById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event with id = " + id + " no found."));
    }

    private User getUserFromAuthentication() {
        return userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
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

    public Event toBusinessEntity(EventEntity entity) {
        Location location = locationService.toBusinessEntity(entity.getLocationEntity());
        User owner = userService.toBusinessEntity(entity.getOwner());
        return Event.builder()
                .id(entity.getId())
                .name(entity.getName())
                .owner(owner)
                .eventRegistrations(entity.getEventRegistrationEntities())
                .date(entity.getDate())
                .cost(entity.getCost())
                .duration(entity.getDuration())
                .location(location)
                .maxPlaces(entity.getMaxPlaces())
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

    private EventEntity updatedFields(EventEntity entityExisting, Event updated) {
        LocationEntity locationEntity = locationService.toEntity(updated.getLocation());
        entityExisting.setName(updated.getName());
        entityExisting.setDate(updated.getDate());
        entityExisting.setDuration(updated.getDuration());
        entityExisting.setCost(updated.getCost());
        entityExisting.setMaxPlaces(updated.getMaxPlaces());
        entityExisting.setLocationEntity(locationEntity);
        return entityExisting;
    }
}
