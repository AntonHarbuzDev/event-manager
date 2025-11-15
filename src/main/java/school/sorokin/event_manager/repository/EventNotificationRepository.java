package school.sorokin.event_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sorokin.event_manager.model.entity.EventNotificationEntity;

@Repository
public interface EventNotificationRepository extends JpaRepository<EventNotificationEntity, Long> {
}
