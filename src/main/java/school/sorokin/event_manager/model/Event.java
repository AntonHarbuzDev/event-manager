package school.sorokin.event_manager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.sorokin.event_manager.model.entity.EventRegistrationEntity;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

    private Long id;
    private String name;
    private User owner;
    private Set<EventRegistrationEntity> eventRegistrations;
    private OffsetDateTime date;
    private int cost;
    private int duration;
    private Location location;
    private int maxPlaces;
    private Status status;
}
