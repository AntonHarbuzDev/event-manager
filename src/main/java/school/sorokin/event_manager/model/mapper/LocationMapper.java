package school.sorokin.event_manager.model.mapper;

import org.springframework.stereotype.Component;
import school.sorokin.event_manager.model.Location;
import school.sorokin.event_manager.model.dto.LocationDto;
import school.sorokin.event_manager.model.entity.LocationEntity;

@Component
public class LocationMapper {

    public Location toBusinessEntity(LocationEntity locationEntity) {
        Location location = new Location();
        location.setId(locationEntity.getId());
        location.setName(locationEntity.getName());
        location.setAddress(locationEntity.getAddress());
        location.setCapacity(locationEntity.getCapacity());
        location.setDescription(locationEntity.getDescription());
        return location;
    }

    public LocationEntity toEntity(Location location) {
        LocationEntity entity = new LocationEntity();
        entity.setId(location.getId());
        entity.setName(location.getName());
        entity.setAddress(location.getAddress());
        entity.setCapacity(location.getCapacity());
        entity.setDescription(location.getDescription());
        return entity;
    }

    public Location toBusinessEntity(LocationDto dto) {
        Location location = new Location();
        location.setId(dto.getId());
        location.setName(dto.getName());
        location.setAddress(dto.getAddress());
        location.setCapacity(dto.getCapacity());
        location.setDescription(dto.getDescription());
        return location;
    }

    public LocationDto toDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setAddress(location.getAddress());
        dto.setCapacity(location.getCapacity());
        dto.setDescription(location.getDescription());
        return dto;
    }

    public LocationEntity updatedFields(LocationEntity locationExisting, Location updated) {
        LocationEntity result = new LocationEntity();
        result.setId(locationExisting.getId());
        result.setName(updated.getName());
        result.setAddress(updated.getAddress());
        result.setCapacity(updated.getCapacity());
        result.setDescription(updated.getDescription());
        return result;
    }
}
