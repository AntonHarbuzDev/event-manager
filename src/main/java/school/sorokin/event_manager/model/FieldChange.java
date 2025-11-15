package school.sorokin.event_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldChange<T> {

    private T oldField;
    private T newField;
}
