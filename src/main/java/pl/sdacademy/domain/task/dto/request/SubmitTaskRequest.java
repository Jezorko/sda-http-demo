package pl.sdacademy.domain.task.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SubmitTaskRequest {
    @NotNull
    private Long id;

    @NotNull
    private String token;

    @NotNull
    private String submitToken;
}
