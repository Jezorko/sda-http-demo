package pl.sdacademy.domain.authorization.dto.response;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class LoginUserResponse {
    private final String token;
    private final String validUntil;
}
