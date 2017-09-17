package pl.sdacademy.domain.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception;
import pl.sdacademy.domain.shared.exceptions.Forbidden403Exception;
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception;
import pl.sdacademy.domain.task.dto.request.SubmitTaskRequest;
import pl.sdacademy.domain.task.dto.response.GetTaskResponse;
import pl.sdacademy.domain.task.dto.response.SubmitTaskResponse;
import pl.sdacademy.infrastructure.properties.ApplicationProperties;
import rx.Observable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.stream;
import static java.util.Optional.*;
import static java.util.stream.Collectors.toList;
import static pl.sdacademy.domain.shared.ApiStatus.*;
import static pl.sdacademy.domain.shared.DataMasker.shortHash;
import static pl.sdacademy.domain.task.Task.TASK_1;
import static rx.Observable.just;
import static rx.Observable.zip;

@Slf4j
@Service
@RequiredArgsConstructor
class TaskService {

    private final static String TASK_PREFIX = "TASK_";

    private final TaskDescriptionResolver taskDescriptionResolver;
    private final ApplicationProperties applicationProperties;

    List<Task.TaskRepresentation> getAllTasks() {
        of(applicationProperties).map(ApplicationProperties::getDebugEnabled)
                                 .filter(TRUE::equals)
                                 .orElseThrow(() -> new Forbidden403Exception(DEBUG_MODE_DISABLED));
        return stream(Task.values()).map(Task::asRepresentation)
                                    .collect(toList());
    }

    Observable<SubmitTaskResponse> submitTask(@RequestBody @NotNull SubmitTaskRequest request) {
        return just(request).doOnNext(this::validateTaskSubmission)
                            .map(SubmitTaskRequest::getId)
                            .map(id -> id + 1)
                            .map(this::getTaskBy)
                            .map(nextTask -> nextTask.orElseThrow(() -> new NotFound404Exception(NO_MORE_TASKS)))
                            .filter(Objects::nonNull)
                            .map(task -> new SubmitTaskResponse(task.getId(), task.getToken()));
    }

    private void validateTaskSubmission(SubmitTaskRequest request) {
        of(getTaskBy(request.getId(), request.getToken())).filter(currentTask -> currentTask.getSubmitToken()
                                                                                            .equals(request.getSubmitToken()))
                                                          .orElseThrow(() -> new BadRequest400Exception(BAD_SUBMIT_TOKEN));
    }

    public Observable<GetTaskResponse> getTask(Long taskId, String taskToken) {
        log.info("Request for task with ID={} and token={}", taskId, shortHash(taskToken));
        return zip(just(taskId),
                   tokenOrFirstTaskToken(taskToken),
                   taskDescriptionResolver.resolveMessageFor(zip(just(taskId),
                                                                 tokenOrFirstTaskToken(taskToken),
                                                                 this::getTaskBy)),
                   GetTaskResponse::new);
    }

    private Observable<String> tokenOrFirstTaskToken(String taskToken) {
        return just(ofNullable(taskToken).orElseGet(TASK_1::getToken));
    }

    private Task getTaskBy(Long taskId, String taskToken) {
        Task task = getTaskBy(taskId).orElseThrow(() -> new NotFound404Exception(TASK_NOT_FOUND));

        if (!Objects.equals(task.getToken(), taskToken)) {
            throw new BadRequest400Exception(INVALID_TASK_TOKEN);
        }

        return task;
    }

    private Optional<Task> getTaskBy(Long taskId) {
        try {
            return of(taskId).map(String::valueOf)
                             .map(TASK_PREFIX::concat)
                             .map(Task::valueOf);
        } catch (IllegalArgumentException e) {
            log.info("Task of ID={} does not exist", taskId);
            return empty();
        }
    }
}
