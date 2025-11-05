package school.sorokin.event_manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sorokin.event_manager.model.dto.EventCreateDto;
import school.sorokin.event_manager.model.dto.EventShowDto;
import school.sorokin.event_manager.model.filter.EventFilter;
import school.sorokin.event_manager.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<EventShowDto> createEvent(@RequestBody @Valid EventCreateDto dto) {
        log.info("Get request for event create: EventDto = {}", dto);
        EventShowDto result = eventService.createEvent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<EventShowDto> getEventById(@PathVariable Long eventId) {
        EventShowDto result = eventService.getEventShowDtoById(eventId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<EventShowDto>> getEventsMy() {
        List<EventShowDto> result = eventService.getEventsCreatedByLoggedUser();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<EventShowDto>> getAllForFilter(@RequestBody EventFilter filter) {
        List<EventShowDto> result = eventService.getEventsByFilter(filter);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<EventShowDto> updateEvent(@RequestBody @Valid EventCreateDto dto,
                                                    @PathVariable Long eventId) {
        log.info("Get request for update event: id = {}, eventDto = {}", eventId, dto);
        EventShowDto result = eventService.updateEvent(dto, eventId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Void> cancelEvent(@PathVariable Long eventId) {
        eventService.cancelEvent(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

