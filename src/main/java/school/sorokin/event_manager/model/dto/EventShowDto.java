package school.sorokin.event_manager.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.sorokin.event_manager.model.Status;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShowDto {

    private Long id;
    private String name;
    private Long ownerId;
    private String ownerName;
    private Long locationId;
    private String locationName;
    private int occupiedPlaces;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime date;
    private int duration;
    private BigDecimal cost;
    private Integer maxPlaces;
    private Status status;
}
