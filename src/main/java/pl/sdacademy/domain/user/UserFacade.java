package pl.sdacademy.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sdacademy.domain.shared.DigestUtil;
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception;
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception;
import rx.Observable;

import java.util.Objects;

import static java.lang.Boolean.FALSE;
import static pl.sdacademy.domain.shared.ApiStatus.INVALID_CREDENTIALS;
import static pl.sdacademy.domain.shared.ApiStatus.USER_NOT_FOUND;
import static rx.Observable.*;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;
    private final DigestUtil digestUtil;

    public Observable<User> getUserValidateCredentials(String username, String password) {
        return just(username).map(userRepository::getByUsername)
                             .filter(Objects::nonNull)
                             .switchIfEmpty(fromCallable(() -> {throw new NotFound404Exception(USER_NOT_FOUND);}))
                             .doOnNext(user -> validatePasswordOf(user, password));
    }

    private void validatePasswordOf(User user, String password) {
        zip(just(user).map(User::getPasswordHash),
            just(password).map(digestUtil::generateHashFrom),
            Objects::equals)
                .filter(FALSE::equals)
                .doOnNext(e -> {throw new BadRequest400Exception(INVALID_CREDENTIALS);})
                .toBlocking()
                .subscribe();
    }

}
