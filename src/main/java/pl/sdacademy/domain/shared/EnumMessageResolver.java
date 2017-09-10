package pl.sdacademy.domain.shared;

import org.springframework.context.MessageSource;
import pl.sdacademy.infrastructure.properties.ClientProperties;
import rx.Observable;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

import static pl.sdacademy.domain.shared.HeaderNames.ACCEPT_LANGUAGE;
import static rx.Observable.*;

public abstract class EnumMessageResolver<T extends Enum<T>> {

    protected abstract MessageSource getMessageSource();

    protected abstract HttpServletRequest getHttpRequest();

    protected abstract ClientProperties getClientProperties();

    protected abstract Supplier<String> getDefaultNameGetter();

    public Observable<String> resolveMessageFor(T t) {
        return resolveMessageFor(just(t));
    }

    public Observable<String> resolveMessageFor(Observable<T> t) {
        return zip(t.filter(Objects::nonNull)
                    .map(T::name)
                    .switchIfEmpty(fromCallable(this::getDefaultNameGetter)
                                           .map(Supplier::get)),
                   just((Object[]) null),
                   fromCallable(this::getHttpRequest)
                           .map(h -> h.getHeader(ACCEPT_LANGUAGE))
                           .filter(Objects::nonNull)
                           .switchIfEmpty(fromCallable(this::getClientProperties)
                                                  .map(ClientProperties::getDefaultLocale))
                           .map(Locale::new),
                   getMessageSource()::getMessage);
    }

}
