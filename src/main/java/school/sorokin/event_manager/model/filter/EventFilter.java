package school.sorokin.event_manager.model.filter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import school.sorokin.event_manager.model.Status;

import java.time.OffsetDateTime;

@EqualsAndHashCode
@Getter
@Setter
public class EventFilter {

    private int durationMax;
    private int durationMin;
    private OffsetDateTime dateStartBefore;
    private OffsetDateTime dateStartAfter;
    private int placesMax;
    private int placesMin;
    private Long locationId;
    private Status eventStatus;
    private String name;
    private int costMax;
    private int costMin;
}
