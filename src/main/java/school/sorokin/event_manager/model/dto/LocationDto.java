package school.sorokin.event_manager.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class LocationDto {

    private Long id;

    @NotBlank(message = "Name must not be empty")
    private String name;

    @NotBlank(message = "Address must not be empty")
    private String address;

    @Min(value = 1, message = "Capacity must be greater than 0")
    private int capacity;

    @NotBlank(message = "Description must not be empty")
    private String description;

    public LocationDto(Long id, String name, String address, int capacity, String description) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LocationDto dto = (LocationDto) o;
        return capacity == dto.capacity && Objects.equals(id, dto.id) && Objects.equals(name, dto.name) && Objects.equals(address, dto.address) && Objects.equals(description, dto.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, capacity, description);
    }
}
