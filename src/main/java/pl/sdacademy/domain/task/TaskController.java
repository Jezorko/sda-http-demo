package pl.sdacademy.domain.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.sdacademy.domain.task.dto.response.GetTaskResponse;
import rx.Observable;

import java.util.List;

import static pl.sdacademy.domain.shared.HeaderNames.TASK_TOKEN;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/tasks")
    public List<Task.TaskRepresentation> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/tasks/{taskId}")
    public Observable<GetTaskResponse> getTask(@PathVariable Long taskId, @RequestHeader(name = TASK_TOKEN, required = false) String taskToken) {
        return taskService.getTask(taskId, taskToken);
    }

}
