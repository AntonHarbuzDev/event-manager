package school.sorokin.event_manager.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.sorokin.event_manager.model.Status;
import school.sorokin.event_manager.model.entity.EventRegistrationEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {

    private Long id;

    @NotBlank(message = "Name must not be empty")
    private String name;

    @NotNull(message = "Owner id must be assigned")
    private Long ownerId;

    private Set<Long> eventRegistrationIds;

    @NotNull(message = "Date must not be empty")
    @Future(message = "Date must be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime date;

    @Min(value = 0, message = "Cost must not be negative")
    @NotNull
    private BigDecimal cost;

    @Min(value = 30, message = "Minimum duration 30 minutes")
    private int duration;

    @NotNull(message = "Location id must be specified")
    private Long locationId;

    @Min(value = 1, message = "The quantity must be greater than 0")
    @NotNull
    private Integer maxPlaces;

    private Status status;
}