package pl.sdacademy.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.sdacademy.domain.shared.DigestUtil;
import pl.sdacademy.domain.shared.exceptions.InternalServer500Exception;
import pl.sdacademy.domain.user.dto.request.CreateUserRequest;

import java.util.Objects;

import static pl.sdacademy.domain.shared.ApiStatus.USER_CREATION_ERROR;
import static rx.Observable.fromCallable;
import static rx.Observable.just;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserService {

    private final UserRepository userRepository;
    private final DigestUtil digestUtil;

    public void createUser(CreateUserRequest request) {
        just(request).map(CreateUserRequest::getUsername)
                     .map(userRepository::getByUsername)
                     .filter(Objects::nonNull)
                     .switchIfEmpty(fromCallable(() -> buildUserFrom(request)))
                     .doOnNext(userRepository::save)
                     .map(User::getId)
                     .toBlocking()
                     .subscribe(id -> log.info("User id is: {}", id),
                                this::throwUserCreatingException);
    }

    private User buildUserFrom(CreateUserRequest request) {
        final User user = User.builder()
                              .username(request.getUsername())
                              .email(request.getEmail())
                              .passwordHash(digestUtil.generateHashFrom(request.getPassword()))
                              .build();
        log.info("A new user has been created: {}", user);
        return user;
    }

    private void throwUserCreatingException(Throwable e) {
        log.error("Exception was thrown while creating user:", e);
        throw new InternalServer500Exception(USER_CREATION_ERROR);
    }
}
