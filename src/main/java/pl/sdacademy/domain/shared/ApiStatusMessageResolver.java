package pl.sdacademy.domain.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import pl.sdacademy.infrastructure.properties.ClientProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

import static pl.sdacademy.domain.shared.ApiStatus.DESCRIPTION_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class ApiStatusMessageResolver extends EnumMessageResolver<ApiStatus> {

    private final MessageSource errorMessages;
    private final HttpServletRequest httpRequest;
    private final ClientProperties clientProperties;

    @Override
    public MessageSource getMessageSource() {
        return errorMessages;
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
        return DESCRIPTION_NOT_FOUND::name;
    }
}
