package school.sorokin.event_manager.model.mapper;

import org.springframework.stereotype.Component;
import school.sorokin.event_manager.model.Location;
import school.sorokin.event_manager.model.dto.LocationDto;
import school.sorokin.event_manager.model.entity.LocationEntity;

@Component
public class LocationMapper {

    public Location toBusinessEntity(LocationEntity locationEntity) {
        return new Location(
                locationEntity.getId(),
                locationEntity.getName(),
                locationEntity.getAddress(),
                locationEntity.getCapacity(),
                locationEntity.getDescription()
        );
    }

    public LocationEntity toEntity(Location location) {
        return new LocationEntity(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getCapacity(),
                location.getDescription()
        );
    }

    public Location toBusinessEntity(LocationDto dto) {
        return new Location(
                dto.getId(),
                dto.getName(),
                dto.getAddress(),
                dto.getCapacity(),
                dto.getDescription()
        );
    }

    public LocationDto toDto(Location location) {
        return new LocationDto(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getCapacity(),
                location.getDescription()
        );
    }

    public LocationEntity updatedFields(LocationEntity locationExisting, LocationDto updated) {
        return new LocationEntity(
                locationExisting.getId(),
                updated.getName(),
                updated.getAddress(),
                updated.getCapacity(),
                updated.getDescription()
        );
    }

    public LocationEntity toEntity(LocationDto locationDto) {
        return new LocationEntity(
                locationDto.getId(),
                locationDto.getName(),
                locationDto.getAddress(),
                locationDto.getCapacity(),
                locationDto.getDescription()
        );
    }

    public LocationDto toDto(LocationEntity locationEntity) {
        return new LocationDto(
                locationEntity.getId(),
                locationEntity.getName(),
                locationEntity.getAddress(),
                locationEntity.getCapacity(),
                locationEntity.getDescription()
        );
    }
}
