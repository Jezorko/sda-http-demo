package pl.sdacademy.domain.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.domain.task.dto.request.SubmitTaskRequest;
import pl.sdacademy.domain.task.dto.response.GetTaskResponse;
import pl.sdacademy.domain.task.dto.response.SubmitTaskResponse;
import rx.Observable;

import javax.validation.constraints.NotNull;
import java.util.List;

import static pl.sdacademy.domain.shared.HeaderNames.TASK_TOKEN;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
class TaskController {
    private final TaskService taskService;

    @GetMapping
    List<Task.TaskRepresentation> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    Observable<SubmitTaskResponse> submitTask(@RequestBody @NotNull SubmitTaskRequest request) {
        return taskService.submitTask(request);
    }

    @GetMapping("/{taskId}")
    Observable<GetTaskResponse> getTask(@PathVariable Long taskId, @RequestHeader(name = TASK_TOKEN, required = false) String taskToken) {
        return taskService.getTask(taskId, taskToken);
    }

}
