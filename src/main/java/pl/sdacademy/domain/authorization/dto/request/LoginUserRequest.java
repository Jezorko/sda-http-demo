package pl.sdacademy.domain.authorization.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ToString(exclude = "password")
public class LoginUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
