package school.sorokin.event_manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sorokin.event_manager.model.Location;
import school.sorokin.event_manager.model.entity.LocationEntity;
import school.sorokin.event_manager.model.mapper.LocationMapper;
import school.sorokin.event_manager.repository.LocationRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Transactional
    public Location create(Location location) {
        LocationEntity locationEntity = locationRepository.save(locationMapper.toEntity(location));
        Location result = locationMapper.toBusinessEntity(locationEntity);
        log.info("Create success {}", result);
        return result;
    }

    @Transactional(readOnly = true)
    public List<Location> getAll() {
        List<LocationEntity> entities = locationRepository.findAll();
        return entities.stream().map(locationMapper::toBusinessEntity).toList();
    }

    @Transactional(readOnly = true)
    public Location getById(Long id) {
        return locationMapper.toBusinessEntity(loadById(id));
    }

    @Transactional
    public Location update(Location location) {
        LocationEntity loaded = loadById(location.getId());
        checkCapacityNotReduced(loaded, location);
        LocationEntity updated = locationMapper.updatedFields(loaded, location);
        Location result = locationMapper.toBusinessEntity(locationRepository.save(updated));
        log.info("Update success {}", result);
        return result;
    }

    @Transactional
    public void delete(Long id) {
        LocationEntity locationEntity = loadById(id);
        locationRepository.delete(locationEntity);
        log.info("Delete success {}", locationEntity);
    }

    private LocationEntity loadById(Long id) {
        return locationRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "Location with id = " + id + " no found."));
    }

    private void checkCapacityNotReduced(LocationEntity loaded, Location updated) {
        if (loaded.getCapacity() > updated.getCapacity()) {
            throw new IllegalArgumentException("You are trying to reduce the capacity at an event");
        }
    }
}
