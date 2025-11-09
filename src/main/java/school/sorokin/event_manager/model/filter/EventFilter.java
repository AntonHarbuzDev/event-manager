package school.sorokin.event_manager.model.filter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import school.sorokin.event_manager.model.Status;

import java.time.OffsetDateTime;

@EqualsAndHashCode
@Getter
@Setter
@ToString
public class EventFilter {

    @Min(0)
    private int durationMax;

    @Min(0)
    private int durationMin;

    private OffsetDateTime dateStartBefore;
    private OffsetDateTime dateStartAfter;

    @Min(0)
    private int placesMax;

    @Min(0)
    private int placesMin;

    @Positive
    private Long locationId;

    private Status eventStatus;
    private String name;

    @Min(0)
    private int costMax;

    @Min(0)
    private int costMin;
}
