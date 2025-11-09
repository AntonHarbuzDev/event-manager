package school.sorokin.event_manager.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sorokin.event_manager.model.dto.EventShowDto;
import school.sorokin.event_manager.service.EventRegistrationService;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
public class EventRegistrationController {

    private static final Logger log = LoggerFactory.getLogger(EventRegistrationController.class);

    private final EventRegistrationService eventRegistrationService;

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> registration(@PathVariable Long eventId) {
        log.info("Get request for registration event id = {}", eventId);
        eventRegistrationService.registrationEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventShowDto>> getRegistrationMy() {
        List<EventShowDto> result = eventRegistrationService.getRegistrationEventByLoggedUser();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long eventId) {
        log.info("Get request for cancel registration event id = {}", eventId);
        eventRegistrationService.cancelRegistration(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
