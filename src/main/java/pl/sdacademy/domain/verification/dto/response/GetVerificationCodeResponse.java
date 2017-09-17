package pl.sdacademy.domain.verification.dto.response;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class GetVerificationCodeResponse {
    private final String verificationCode;
}
