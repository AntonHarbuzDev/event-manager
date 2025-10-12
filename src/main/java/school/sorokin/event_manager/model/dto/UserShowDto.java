package school.sorokin.event_manager.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import school.sorokin.event_manager.model.Role;

import java.util.Objects;

public class UserShowDto {

    private Long id;
    private String login;
    private int age;
    private Role role;

    public UserShowDto(Long id, String login, int age, Role role) {
        this.id = id;
        this.login = login;
        this.age = age;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserShowDto that = (UserShowDto) o;
        return age == that.age && Objects.equals(id, that.id) && Objects.equals(login, that.login) && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, age, role);
    }

    @Override
    public String toString() {
        return "UserShowDto{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", age=" + age +
                ", role=" + role +
                '}';
    }
}
