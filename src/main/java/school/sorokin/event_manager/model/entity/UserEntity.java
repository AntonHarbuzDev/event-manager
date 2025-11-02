package school.sorokin.event_manager.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import school.sorokin.event_manager.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, name = "login")
    @NotBlank(message = "Login must not be empty")
    private String login;

    @Column(name = "password")
    @NotBlank(message = "Password must not be empty")
    @ToString.Exclude
    private String password;

    @Column(name = "age")
    @Min(value = 18, message = "Age must be of legal")
    @Max(value = 150, message = "Age must be realistic")
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
}
