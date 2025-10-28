package school.sorokin.event_manager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

    private Long id;
    private String name;
    private User owner;
    private Set<EventRegistration> eventRegistrations;
    private LocalDateTime date;
    private int cost;
    private int duration;
    private Location location;
    private int maxPlaces;
    private Status status;
}
