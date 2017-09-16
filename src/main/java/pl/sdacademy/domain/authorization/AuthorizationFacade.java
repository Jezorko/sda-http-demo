package pl.sdacademy.domain.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sdacademy.domain.shared.exceptions.Forbidden403Exception;
import pl.sdacademy.domain.user.User;
import rx.Observable;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static pl.sdacademy.domain.shared.ApiStatus.INVALID_CREDENTIALS;
import static pl.sdacademy.domain.shared.HeaderNames.AUTHORIZATION_TOKEN;
import static rx.Observable.error;
import static rx.Observable.just;

@Component
@RequiredArgsConstructor
public class AuthorizationFacade {

    private final HttpServletRequest httpServletRequest;
    private final AuthorizationTokenRepository authorizationTokenRepository;

    public Observable<User> getByToken() {
        return just(httpServletRequest).map(r -> r.getHeader(AUTHORIZATION_TOKEN))
                                       .map(authorizationTokenRepository::findByValue)
                                       .filter(Objects::nonNull)
                                       .map(AuthorizationToken::getUser)
                                       .switchIfEmpty(error(new Forbidden403Exception(INVALID_CREDENTIALS)));
    }
}
