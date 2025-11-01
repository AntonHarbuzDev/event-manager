package school.sorokin.event_manager.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sorokin.event_manager.model.Status;
import school.sorokin.event_manager.model.User;
import school.sorokin.event_manager.model.dto.EventShowDto;
import school.sorokin.event_manager.model.entity.EventEntity;
import school.sorokin.event_manager.model.entity.EventRegistrationEntity;
import school.sorokin.event_manager.model.entity.UserEntity;
import school.sorokin.event_manager.repository.EventRegistrationRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EventRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(EventRegistrationService.class);

    private final EventRegistrationRepository eventRegistrationRepository;
    private final UserService userService;
    private final EventService eventService;

    @Transactional
    public void registrationEvent(Long id) {
        EventEntity eventEntity = eventService.getEventEntityById(id);
        checkStatusNoCancelled(eventEntity);
        checkStatusNoFinish(eventEntity);
        checkCapacity(eventEntity);
        checkUserAlreadyRegistered(eventEntity);
        User user = getUserFromAuthentication();
        UserEntity userEntity = userService.toEntity(user);
        checkOwnerEvent(eventEntity, userEntity);

        EventRegistrationEntity eventRegistrationEntity = new EventRegistrationEntity(null, userEntity, eventEntity);
        EventRegistrationEntity result = eventRegistrationRepository.save(eventRegistrationEntity);
        log.info("User = {} registered from event = {}",
                result.getUserEntity().getLogin(), result.getEventEntity().getName());
    }

    @Transactional(readOnly = true)
    public List<EventShowDto> getRegistrationEventByLoggedUser() {
        return loadRegistrationEventByLoggedUser();
    }

    @Transactional
    public void cancelRegistration(Long id) {
        EventEntity eventEntity = eventService.getEventEntityById(id);
        checkStatusNoFinish(eventEntity);
        checkStatusNoStarted(eventEntity);
        checkUserNoRegistered(eventEntity);
        User user = getUserFromAuthentication();
        UserEntity userEntity = userService.toEntity(user);
        EventRegistrationEntity eventRegistrationEntity = eventRegistrationRepository.findByUserEntityAndEventEntity(userEntity, eventEntity)
                .orElseThrow(() -> new NoSuchElementException(String.format("User = %s is not registered for the event = %s.", userEntity.getLogin(), eventEntity.getName())));
        eventRegistrationRepository.delete(eventRegistrationEntity);
        log.info("User = {} has been removed from the event = {}", userEntity.getLogin(), eventEntity.getName());
    }

    private void checkStatusNoCancelled(EventEntity eventEntity) {
        if (eventEntity.getStatus().equals(Status.CANCELLED)) {
            throw new IllegalArgumentException(String.format("The event = %s is cancelled", eventEntity.getName()));
        }
    }

    private void checkStatusNoFinish(EventEntity eventEntity) {
        if (eventEntity.getStatus().equals(Status.FINISHED)) {
            throw new IllegalArgumentException(String.format("The event = %s is already over", eventEntity.getName()));
        }
    }

    private void checkStatusNoStarted(EventEntity eventEntity) {
        if (eventEntity.getStatus().equals(Status.STARTED)) {
            throw new IllegalArgumentException(String.format("The event = %s is already begin", eventEntity.getName()));
        }
    }

    private void checkUserAlreadyRegistered(EventEntity eventEntity) {
        if (loadRegistrationEventByLoggedUser().stream().anyMatch(e -> e.getId().equals(eventEntity.getId()))) {
            throw new IllegalArgumentException(String.format("The user already register from event = %s",
                    eventEntity.getName()));
        }
    }

    private void checkUserNoRegistered(EventEntity eventEntity) {
        if (loadRegistrationEventByLoggedUser().stream().noneMatch(e -> e.getId().equals(eventEntity.getId()))) {
            throw new IllegalArgumentException(String.format("The user no register from event = %s",
                    eventEntity.getName()));
        }
    }

    private void checkCapacity(EventEntity eventEntity) {
        Long count = eventRegistrationRepository.countByEventEntity(eventEntity);
        if (eventEntity.getMaxPlaces() <= count) {
            throw new IllegalArgumentException(String.format("The event = %s no free space", eventEntity.getName()));
        }
    }

    private void checkOwnerEvent(EventEntity eventEntity, UserEntity userEntity) {
        if (eventEntity.getOwner().getId().equals(userEntity.getId())) {
            throw new IllegalArgumentException(String.format(
                    "The owner cannot register for his event = %s", eventEntity.getName()));
        }
    }

    private List<EventShowDto> loadRegistrationEventByLoggedUser() {
        User user = getUserFromAuthentication();
        List<EventRegistrationEntity> eventRegistrationEntities =
                eventRegistrationRepository.findByUserEntity(userService.toEntity(user));
        return eventRegistrationEntities.stream()
                .map(EventRegistrationEntity::getEventEntity).map(eventService::toShowDto).toList();
    }

    private User getUserFromAuthentication() {
        return userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
