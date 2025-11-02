package school.sorokin.event_manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import school.sorokin.event_manager.model.Role;

@Data
@AllArgsConstructor
public class UserShowDto {

    private Long id;
    private String login;
    private int age;
    private Role role;
}
