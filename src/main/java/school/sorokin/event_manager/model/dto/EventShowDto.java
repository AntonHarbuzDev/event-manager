package school.sorokin.event_manager.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import school.sorokin.event_manager.model.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShowDto {

    private Long id;
    private Long ownerId;
    private String name;
    private Long locationId;
    private int occupiedPlaces;
    @DateTimeFormat(pattern = "${pattern.date-time}")
    private LocalDateTime date;
    private int duration;
    private int cost;
    private int maxPlaces;
    private Status status;
}
