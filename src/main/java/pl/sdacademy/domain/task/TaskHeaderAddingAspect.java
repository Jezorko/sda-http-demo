package pl.sdacademy.domain.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pl.sdacademy.domain.authorization.dto.response.LoginUserResponse;
import rx.Observable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Optional.of;
import static pl.sdacademy.domain.shared.HeaderNames.ACCEPT_LANGUAGE;
import static pl.sdacademy.domain.shared.HeaderNames.TASK_SUBMIT_TOKEN;
import static pl.sdacademy.domain.task.Task.*;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TaskHeaderAddingAspect {

    private final HttpServletRequest httpRequest;
    private final HttpServletResponse httpResponse;

    @After("execution(public * pl.sdacademy.domain.task.TaskService.getTask(..))")
    public void task1Completed() {
        of(httpRequest).map(r -> r.getHeader(ACCEPT_LANGUAGE))
                       .ifPresent(h -> addSubmitTokenOf(TASK_1));
    }

    @After("execution(public void pl.sdacademy.domain.user.CreateUserService.createUser(..))")
    public void task2Completed() {
        addSubmitTokenOf(TASK_2);
    }

    @AfterReturning(value = "execution(public * pl.sdacademy.domain.authorization.AuthorizationService.login(..))", returning = "result")
    public void task3Completed(Observable<LoginUserResponse> result) {
        result.subscribe(r -> addSubmitTokenOf(TASK_3));
    }

    private void addSubmitTokenOf(Task task) {
        log.info(task.name() + " completed");
        httpResponse.addHeader(TASK_SUBMIT_TOKEN, task.getSubmitToken());
    }

}
