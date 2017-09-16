package pl.sdacademy.domain.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.sdacademy.domain.authorization.dto.request.LoginUserRequest;
import pl.sdacademy.domain.authorization.dto.response.LoginUserResponse;
import pl.sdacademy.domain.user.User;
import pl.sdacademy.domain.user.UserFacade;
import rx.Observable;

import java.util.Objects;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static rx.Observable.fromCallable;
import static rx.Observable.just;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserFacade userFacade;
    private final AuthorizationTokenRepository authorizationTokenRepository;

    Observable<LoginUserResponse> login(LoginUserRequest request) {
        return userFacade.getUserValidateCredentials(request.getUsername(), request.getPassword())
                         .flatMap(u -> just(u).map(authorizationTokenRepository::findByUser)
                                              .filter(Objects::nonNull)
                                              .filter(this::isNotExpired)
                                              .switchIfEmpty(fromCallable(() -> createToken(u))))
                         .map(authorizationTokenRepository::save)
                         .map(this::creteResponse);
    }

    private boolean isNotExpired(AuthorizationToken authorizationToken) {
        if (authorizationToken.getExpirationDate()
                              .isBefore(now())) {
            authorizationTokenRepository.delete(authorizationToken);
            return false;
        }
        return true;
    }

    private AuthorizationToken createToken(User user) {
        return AuthorizationToken.builder()
                                 .user(user)
                                 .value(randomAlphanumeric(10))
                                 .expirationDate(now().plusHours(1))
                                 .build();
    }

    private LoginUserResponse creteResponse(AuthorizationToken authorizationToken) {
        return LoginUserResponse.builder()
                                .token(authorizationToken.getValue())
                                .validUntil(authorizationToken.getExpirationDate()
                                                              .toString())
                                .build();
    }
}
