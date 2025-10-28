package school.sorokin.event_manager.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventCreateDto {

    @NotBlank(message = "Name must not be empty")
    private String name;

    @NotNull(message = "Date must not be empty")
    @Future(message = "Date must be in the future")
    @DateTimeFormat(pattern = "${pattern.date-time}")
    private LocalDateTime date;

    @Min(value = 0, message = "Cost must be positive")
    private int cost;

    @Min(value = 30, message = "Minimum duration 30 minutes")
    private int duration;

    @NotNull(message = "Location id must be specified")
    private Long locationId;

    @Min(value = 1, message = "The quantity must be greater than 0")
    private int maxPlaces;
}
