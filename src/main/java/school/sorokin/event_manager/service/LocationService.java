package school.sorokin.event_manager.service;

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

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Transactional
    public Location create(Location location) {
        LocationEntity result = locationRepository.save(locationMapper.toEntity(location));
        return locationMapper.toBusinessEntity(result);
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
        return locationMapper.toBusinessEntity(locationRepository.save(updated));
    }

    @Transactional
    public void delete(Long id) {
        locationRepository.delete(loadById(id));
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
