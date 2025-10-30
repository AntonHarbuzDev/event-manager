package school.sorokin.event_manager.service.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import school.sorokin.event_manager.model.entity.EventEntity;
import school.sorokin.event_manager.model.filter.EventFilter;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventSpecifications {

    public Specification<EventEntity> withFilter(EventFilter filter) {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getDurationMax() > 0) {
                predicates.add(cb.lessThanOrEqualTo(root.get("duration"), filter.getDurationMax()));
            }

            if (filter.getDurationMin() > 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("duration"), filter.getDurationMin()));
            }

            if (filter.getDateStartAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filter.getDateStartAfter()));
            }

            if (filter.getDateStartBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), filter.getDateStartBefore()));
            }

            if (filter.getPlacesMin() > 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("maxPlaces"), filter.getPlacesMin()));
            }

            if (filter.getPlacesMax() > 0) {
                predicates.add(cb.lessThanOrEqualTo(root.get("maxPlaces"), filter.getPlacesMax()));
            }

            if (filter.getLocationId() != null) {
                predicates.add(cb.equal(root.get("locationEntity").get("id"), filter.getLocationId()));
            }

            if (filter.getEventStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getEventStatus()));
            }

            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"
                ));
            }

            if (filter.getCostMin() > 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("cost"), filter.getCostMin()));
            }
            if (filter.getCostMax() > 0) {
                predicates.add(cb.lessThanOrEqualTo(root.get("cost"), filter.getCostMax()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
