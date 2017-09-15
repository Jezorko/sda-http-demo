package pl.sdacademy.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ToString(exclude = "password")
public class CreateUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String email;
}
