package school.sorokin.event_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@AllArgsConstructor
public class User {

    @EqualsAndHashCode.Include
    private Long id;
    private String login;
    private String password;
    private int age;
    private Role role;
}
