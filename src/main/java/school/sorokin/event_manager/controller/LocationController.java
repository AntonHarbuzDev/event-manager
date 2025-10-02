package school.sorokin.event_manager.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sorokin.event_manager.model.Location;
import school.sorokin.event_manager.model.dto.LocationDto;
import school.sorokin.event_manager.model.mapper.LocationMapper;
import school.sorokin.event_manager.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final LocationMapper locationMapper;

    public LocationController(LocationService locationService, LocationMapper locationMapper) {
        this.locationService = locationService;
        this.locationMapper = locationMapper;
    }

    @PostMapping
    public ResponseEntity<LocationDto> create(@RequestBody @Valid LocationDto dto) {
        Location created = locationService.create(locationMapper.toBusinessEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(locationMapper.toDto(created));
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAll() {
        List<LocationDto> result = locationService.getAll().stream().map(locationMapper::toDto).toList();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getById(@PathVariable Long id) {
        Location location = locationService.getById(id);
        return ResponseEntity.ok().body(locationMapper.toDto(location));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> update(@PathVariable Long id, @RequestBody @Valid LocationDto dto) {
        Location locationToUpdate = locationMapper.toBusinessEntity(dto);
        locationToUpdate.setId(id);
        Location updated = locationService.update(locationToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(locationMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
