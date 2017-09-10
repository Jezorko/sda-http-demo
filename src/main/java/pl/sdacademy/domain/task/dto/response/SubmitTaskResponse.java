package pl.sdacademy.domain.task.dto.response;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class SubmitTaskResponse {
    private final Long nextTaskId;
    private final String nextTaskToken;
}
