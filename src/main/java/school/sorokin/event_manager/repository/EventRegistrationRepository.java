package school.sorokin.event_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sorokin.event_manager.model.entity.EventEntity;
import school.sorokin.event_manager.model.entity.EventRegistrationEntity;
import school.sorokin.event_manager.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistrationEntity, Long> {

    List<EventRegistrationEntity> findByUserEntity(UserEntity userEntity);

    Long countByEventEntity(EventEntity eventEntity);

    Optional<EventRegistrationEntity> findByUserEntityAndEventEntity(UserEntity userEntity, EventEntity eventEntity);
}
