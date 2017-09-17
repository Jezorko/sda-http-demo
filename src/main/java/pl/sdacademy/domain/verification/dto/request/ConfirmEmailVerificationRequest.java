package pl.sdacademy.domain.verification.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ToString
public class ConfirmEmailVerificationRequest {
    @NotBlank
    private String verificationCode;
}
