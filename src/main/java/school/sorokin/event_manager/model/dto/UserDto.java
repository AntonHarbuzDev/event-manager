package school.sorokin.event_manager.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import school.sorokin.event_manager.model.Role;

@Data
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "Login must not be empty")
    private String login;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 4, message = "Password must be at least 4 characters")
    @ToString.Exclude
    private String password;

    @Min(value = 18, message = "Age must be of legal")
    @Max(value = 150, message = "Age must be realistic")
    private int age;

    private Role role;
}
