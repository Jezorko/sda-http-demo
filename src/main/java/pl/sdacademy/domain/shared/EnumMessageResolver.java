package pl.sdacademy.domain.shared;

import org.springframework.context.MessageSource;
import rx.Observable;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.function.Supplier;

import static rx.Observable.*;

public abstract class EnumMessageResolver<T extends Enum<T>> {

    protected abstract MessageSource getMessageSource();

    protected abstract HttpServletRequest getHttpRequest();

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
                           .map(ServletRequest::getLocale),
                   getMessageSource()::getMessage);
    }

}
