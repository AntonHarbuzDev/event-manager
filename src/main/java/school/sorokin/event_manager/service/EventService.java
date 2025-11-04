package school.sorokin.event_manager.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sorokin.event_manager.model.Status;
import school.sorokin.event_manager.model.dto.EventCreateDto;
import school.sorokin.event_manager.model.dto.EventShowDto;
import school.sorokin.event_manager.model.entity.EventEntity;
import school.sorokin.event_manager.model.entity.LocationEntity;
import school.sorokin.event_manager.model.entity.UserEntity;
import school.sorokin.event_manager.model.filter.EventFilter;
import school.sorokin.event_manager.repository.EventRepository;
import school.sorokin.event_manager.repository.LocationRepository;
import school.sorokin.event_manager.repository.UserRepository;
import school.sorokin.event_manager.service.specification.EventSpecifications;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final EventSpecifications eventSpecifications;

    @Transactional
    public EventShowDto createEvent(EventCreateDto dto, String username) {
        EventEntity event = toEntity(dto);
        checkCapacityLocation(event);
        UserEntity ownerEntity = userRepository.findByLogin(username).orElseThrow();
        event.setOwner(ownerEntity);
        event.setStatus(Status.WAIT_START);
        event.setEventRegistrationEntities(new HashSet<>());
        EventEntity result = eventRepository.save(event);
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

    @Transactional(readOnly = true)
    public EventEntity getEventEntityById(Long id) {
        return loadEventEntityById(id);
    }

    @Transactional(readOnly = true)
    public List<EventShowDto> getEventsCreatedByLoggedUser() {
        UserEntity userEntity = getUserFromAuthentication();
        List<EventEntity> events = eventRepository.findAllByOwner(userEntity);
        return events.stream().map(this::toShowDto).toList();
    }

    @Transactional
    public List<EventShowDto> getEventsByFilter(EventFilter eventFilter) {
        Specification<EventEntity> spec = eventSpecifications.withFilter(eventFilter);
        List<EventEntity> eventEntities = eventRepository.findAll(spec);
        if (eventEntities.isEmpty()) {
            throw new NoSuchElementException("The entity was not found with the given filter parameters.");
        }
        return eventEntities.stream().map(this::toShowDto).toList();
    }

    @Transactional
    public List<EventEntity> getEventsForChangeStatus(Status status, OffsetDateTime offsetDateTime) {
        log.info("new date = {}", offsetDateTime);
        List<EventEntity> eventEntities = eventRepository.findAllByStatusAndDateBefore(status, offsetDateTime);
        if (eventEntities.isEmpty()) {
            log.info("There are no events to change the status...");
        } else {
            eventEntities.forEach(e -> log.info("Get event prepare to change status : {}", e));
        }
        return eventEntities;
    }

    @Transactional
    public void updateEventStatus(EventEntity eventEntity, Status status) {
        eventEntity.setStatus(status);
        eventRepository.save(eventEntity);
        log.info("The event = {} status has been switched to {}", eventEntity.getName(), status);
    }

    @Transactional
    public EventShowDto updateEvent(EventCreateDto dto, Long id) {
        EventEntity event = toEntity(dto);
        checkCapacityLocation(event);
        EventEntity loadedEventEntity = loadEventEntityById(id);
        UserEntity userLogin = getUserFromAuthentication();
        checkAlreadyRegistered(loadedEventEntity, dto);
        checkStatusWait(loadedEventEntity);
        checkOwer(loadedEventEntity, userLogin);
        EventEntity updated = updatedFields(loadedEventEntity, event);
        EventEntity result = eventRepository.save(updated);
        log.info("Update event success {}", result);
        return toShowDto(result);
    }

    @Transactional
    public void cancelEvent(Long id) {
        EventEntity loaded = loadEventEntityById(id);
        checkOwer(loaded, getUserFromAuthentication());
        loaded.setStatus(Status.CANCELLED);
        log.info("Cancel event {} success", loaded);
    }

    private void checkCapacityLocation(EventEntity event) {
        if (event.getLocationEntity().getCapacity() < event.getMaxPlaces()) {
            throw new IllegalArgumentException(String.format("The location = %s cannot accommodate %d people.",
                    event.getLocationEntity().getName(), event.getDuration()));
        }
    }

    private void checkOwer(EventEntity event, UserEntity userLogin) {
        if (!event.getOwner().getId().equals(userLogin.getId())) {
            throw new AuthorizationDeniedException(String.format(
                    "User login = %s no owner from event = %s in owner = %s",
                    userLogin, event.getName(), event.getOwner()));
        }
    }

    private void checkAlreadyRegistered(EventEntity eventLoaded, EventCreateDto dto) {
        int registeredAlreadyNumber = eventLoaded.getEventRegistrationEntities().size();
        if (registeredAlreadyNumber > dto.getMaxPlaces()) {
            throw new IllegalArgumentException("The number of people already registered = " + registeredAlreadyNumber);
        }
    }

    private void checkStatusWait(EventEntity eventEntity) {
        if (!eventEntity.getStatus().equals(Status.WAIT_START)) {
            throw new IllegalArgumentException(String.format("Status has been %s", Status.WAIT_START));
        }
    }

    private EventEntity loadEventEntityById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event with id = " + id + " no found."));
    }

    private UserEntity getUserFromAuthentication() {
        return userRepository.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new NoSuchElementException("on user with name "));
    }

    private EventEntity toEntity(EventCreateDto dto) {
        LocationEntity locationEntity =
                locationRepository.findById(dto.getLocationId())
                        .orElseThrow(() -> new NoSuchElementException("on location with id = " + dto.getLocationId())); // думаю плохо брать из репозитория
        return EventEntity.builder()
                .name(dto.getName())
                .date(dto.getDate())
                .cost(dto.getCost())
                .duration(dto.getDuration())
                .locationEntity(locationEntity)
                .maxPlaces(dto.getMaxPlaces())
                .build();
    }

    public EventShowDto toShowDto(EventEntity eventEntity) {
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

    private EventEntity updatedFields(EventEntity entityExisting, EventEntity updated) {
        entityExisting.setName(updated.getName());
        entityExisting.setDate(updated.getDate());
        entityExisting.setDuration(updated.getDuration());
        entityExisting.setCost(updated.getCost());
        entityExisting.setMaxPlaces(updated.getMaxPlaces());
        entityExisting.setLocationEntity(updated.getLocationEntity());
        return entityExisting;
    }
}
