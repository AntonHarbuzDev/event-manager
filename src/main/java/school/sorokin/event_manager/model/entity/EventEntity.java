package school.sorokin.event_manager.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import school.sorokin.event_manager.model.Status;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity owner;

    @OneToMany(mappedBy = "eventEntity")
    private Set<EventRegistrationEntity> eventRegistrationEntities = new HashSet<>();

    @Column(name = "date")
    @DateTimeFormat(pattern = "${pattern.date-time}")
    private OffsetDateTime date;

    @Column(name = "cost")
    private int cost;

    @Column(name = "duration")
    private int duration;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity locationEntity;

    @Column(name = "maxPlaces")
    private int maxPlaces;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
