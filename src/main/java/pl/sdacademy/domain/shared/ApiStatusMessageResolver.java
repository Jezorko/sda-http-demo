package pl.sdacademy.domain.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

import static pl.sdacademy.domain.shared.ApiStatus.DESCRIPTION_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class ApiStatusMessageResolver extends EnumMessageResolver<ApiStatus> {

    private final MessageSource statusesDescriptions;
    private final HttpServletRequest httpRequest;

    @Override
    public MessageSource getMessageSource() {
        return statusesDescriptions;
    }

    @Override
    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    @Override
    public Supplier<String> getDefaultNameGetter() {
        return DESCRIPTION_NOT_FOUND::name;
    }
}
