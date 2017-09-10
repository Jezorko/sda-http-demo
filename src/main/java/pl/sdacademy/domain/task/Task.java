package pl.sdacademy.domain.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Slf4j
@Getter
@ToString
@RequiredArgsConstructor
enum Task {
    TASK_1,
    TASK_2;

    private final String token = randomAlphanumeric(10);

    TaskRepresentation asRepresentation() {
        return new TaskRepresentation(name(), getToken());
    }

    @Value
    @RequiredArgsConstructor(access = PRIVATE)
    static class TaskRepresentation {
        private final String name;
        private final String token;
    }
}
