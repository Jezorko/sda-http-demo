package pl.sdacademy.domain.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponseOnException {
    private final ApiStatus status;
    private final String description;
}
