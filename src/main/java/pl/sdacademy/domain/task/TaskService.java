package pl.sdacademy.domain.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception;
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception;
import pl.sdacademy.domain.task.dto.response.GetTaskResponse;
import rx.Observable;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static pl.sdacademy.domain.shared.ApiStatus.INVALID_TASK_TOKEN;
import static pl.sdacademy.domain.shared.ApiStatus.TASK_NOT_FOUND;
import static pl.sdacademy.domain.shared.DataMasker.shortHash;
import static pl.sdacademy.domain.task.Task.TASK_1;
import static rx.Observable.just;
import static rx.Observable.zip;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final static String TASK_PREFIX = "TASK_";

    private final TaskDescriptionResolver taskDescriptionResolver;

    List<Task.TaskRepresentation> getAllTasks() {
        return stream(Task.values()).map(Task::asRepresentation)
                                    .collect(toList());
    }

    Observable<GetTaskResponse> getTask(Long taskId, String taskToken) {
        log.info("Request for task with ID={} and token={}", taskId, shortHash(taskToken));
        return zip(just(taskId),
                   taskDescriptionResolver.resolveMessageFor(zip(just(taskId),
                                                                 just(ofNullable(taskToken).orElseGet(TASK_1::getToken)),
                                                                 this::getTaskBy)),
                   GetTaskResponse::new);
    }

    private Task getTaskBy(Long taskId, String taskToken) {
        try {
            return of(taskId).map(String::valueOf)
                             .map(TASK_PREFIX::concat)
                             .map(Task::valueOf)
                             .filter(t -> taskToken.equals(t.getToken()))
                             .orElseThrow(() -> new BadRequest400Exception(INVALID_TASK_TOKEN));
        } catch (IllegalArgumentException e) {
            log.error("Task of ID={} does not exist", taskId);
            throw new NotFound404Exception(TASK_NOT_FOUND);
        }
    }
}
