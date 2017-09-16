package pl.sdacademy.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sdacademy.domain.shared.validation.OptionalEmail;
import pl.sdacademy.domain.shared.validation.OptionalNotBlank;

@Getter
@Setter
@ToString(exclude = "password")
public class UpdateUserRequest {
    @OptionalNotBlank
    private String username;

    @OptionalNotBlank
    private String password;

    @OptionalEmail
    private String email;
}
