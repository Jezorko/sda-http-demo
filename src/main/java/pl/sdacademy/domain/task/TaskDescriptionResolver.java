package pl.sdacademy.domain.task;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import pl.sdacademy.domain.shared.EnumMessageResolver;
import pl.sdacademy.infrastructure.properties.ClientProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
class TaskDescriptionResolver extends EnumMessageResolver<Task> {

    private final MessageSource taskDescriptions;
    private final HttpServletRequest httpRequest;
    private final ClientProperties clientProperties;

    @Override
    public MessageSource getMessageSource() {
        return taskDescriptions;
    }

    @Override
    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    @Override
    public ClientProperties getClientProperties() {
        return clientProperties;
    }

    @Override
    public Supplier<String> getDefaultNameGetter() {
        return String::new;
    }
}
