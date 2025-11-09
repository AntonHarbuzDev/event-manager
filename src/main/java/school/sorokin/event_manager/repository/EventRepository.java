package school.sorokin.event_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import school.sorokin.event_manager.model.Status;
import school.sorokin.event_manager.model.entity.EventEntity;
import school.sorokin.event_manager.model.entity.UserEntity;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    List<EventEntity> findAllByOwner(UserEntity userEntity);

    List<EventEntity> findAllByStatusAndDateBefore(Status status, OffsetDateTime date);
}
