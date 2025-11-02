package school.sorokin.event_manager.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class SingInRequest {

    @NotBlank(message = "Login must not be empty")
    private String login;

    @NotBlank(message = "Password must not be empty")
    @ToString.Exclude
    private String password;
}
