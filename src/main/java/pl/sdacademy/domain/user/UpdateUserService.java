package pl.sdacademy.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.sdacademy.domain.authorization.AuthorizationFacade;
import pl.sdacademy.domain.shared.DigestUtil;
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception;
import pl.sdacademy.domain.user.dto.request.UpdateUserRequest;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.Objects;

import static java.util.Optional.of;
import static pl.sdacademy.domain.shared.ApiStatus.INTERNAL_SERVER_ERROR;
import static pl.sdacademy.domain.shared.ApiStatus.USER_ALREADY_EXISTS;
import static pl.sdacademy.domain.shared.RxJavaCommonFunctions.throwCustomExceptionOrElse;
import static rx.Observable.error;
import static rx.Observable.just;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateUserService {

    private static final Long ZERO = 0L;

    private final UserRepository userRepository;
    private final AuthorizationFacade authorizationFacade;
    private final DigestUtil digestUtil;

    public void updateUser(UpdateUserRequest request) {
        authorizationFacade.getByToken()
                           .map(u -> updateUser(u, request))
                           .map(userRepository::save)
                           .map(User::getId)
                           .toBlocking()
                           .subscribe(id -> log.info("User with id {} has been updated", id),
                                      throwCustomExceptionOrElse(INTERNAL_SERVER_ERROR));

    }

    private User updateUser(User user, UpdateUserRequest request) {
        updateParameter(request.getUsername(), userRepository::countUsersByUsername, user::setUsername);
        updateParameter(request.getEmail(), userRepository::countUsersByEmail, user::setEmail);
        of(request).map(UpdateUserRequest::getPassword)
                   .map(digestUtil::generateHashFrom)
                   .ifPresent(user::setPasswordHash);
        return user;
    }

    private void updateParameter(String parameter, Func1<String, Long> counter, Action1<String> setter) {
        just(parameter).filter(Objects::nonNull)
                       .flatMap(email -> ensureIsNotPresentYet(email, counter))
                       .toBlocking()
                       .subscribe(setter, throwCustomExceptionOrElse(INTERNAL_SERVER_ERROR));
    }

    private Observable<String> ensureIsNotPresentYet(String data, Func1<String, Long> counter) {
        return just(data).filter(u -> ZERO.equals(counter.call(u)))
                         .switchIfEmpty(error(new BadRequest400Exception(USER_ALREADY_EXISTS)));
    }
}
