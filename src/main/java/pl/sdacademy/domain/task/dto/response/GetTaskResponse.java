package pl.sdacademy.domain.task.dto.response;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class GetTaskResponse {
    private final Long id;
    private final String token;
    private final String description;
}
