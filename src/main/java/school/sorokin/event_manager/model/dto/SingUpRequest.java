package school.sorokin.event_manager.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class SingUpRequest {

    @NotBlank(message = "Login must not be empty")
    private String login;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    @Min(value = 18, message = "The user must be of legal age")
    @Max(value = 150, message = "Age must be realistic")
    private int age;

    public SingUpRequest(String login, String password, int age) {
        this.login = login;
        this.password = password;
        this.age = age;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SingUpRequest that = (SingUpRequest) o;
        return age == that.age && Objects.equals(login, that.login) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, age);
    }

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
