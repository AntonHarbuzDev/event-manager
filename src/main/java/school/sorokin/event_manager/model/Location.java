package school.sorokin.event_manager.model;

import java.util.Objects;

public class Location {

    private Long id;
    private String name;
    private String address;
    private int capacity;
    private String description;

    public Location() {
    }

    public Location(Long id, String name, String address, int capacity, String description) {
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
        Location location = (Location) o;
        return capacity == location.capacity && Objects.equals(id, location.id) && Objects.equals(name, location.name) && Objects.equals(address, location.address) && Objects.equals(description, location.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, capacity, description);
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", capacity=" + capacity +
                ", description='" + description + '\'' +
                '}';
    }
}
