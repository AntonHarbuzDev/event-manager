package school.sorokin.event_manager.controller;

import jakarta.validation.Valid;
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
import school.sorokin.event_manager.model.dto.LocationDto;
import school.sorokin.event_manager.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@RequestBody @Valid LocationDto dto) {
        log.info("Get request for location create: LocationDto = {}", dto);
        LocationDto created = locationService.createLocation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        log.info("Get request for get all locations");
        List<LocationDto> result = locationService.getAllLocations();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable Long id) {
        log.info("Get request for get location id = {}", id);
        LocationDto location = locationService.getLocationDtoById(id);
        return ResponseEntity.ok().body(location);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Long id, @RequestBody @Valid LocationDto dto) {
        log.info("Get request for update location: id = {}, LocationDto = {}", id, dto);
        LocationDto updated = locationService.updateLocation(dto, id);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.info("Get request for delete location id = {}", id);
        locationService.deleteLocation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
