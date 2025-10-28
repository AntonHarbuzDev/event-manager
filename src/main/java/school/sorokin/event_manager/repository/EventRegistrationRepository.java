package school.sorokin.event_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sorokin.event_manager.model.entity.EventRegistrationEntity;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistrationEntity, Long> {
}
