package pl.sdacademy.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.sdacademy.domain.shared.DigestUtil;
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception;
import pl.sdacademy.domain.user.dto.request.CreateUserRequest;

import java.util.Objects;

import static pl.sdacademy.domain.shared.ApiStatus.USER_ALREADY_EXISTS;
import static pl.sdacademy.domain.shared.ApiStatus.USER_CREATION_ERROR;
import static pl.sdacademy.domain.shared.RxJavaCommonFunctions.throwCustomExceptionOrElse;
import static rx.Observable.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserService {

    private final UserRepository userRepository;
    private final DigestUtil digestUtil;

    public void createUser(CreateUserRequest request) {
        zip(just(request).map(CreateUserRequest::getUsername),
            just(request).map(CreateUserRequest::getEmail),
            userRepository::getByUsernameOrEmail)
                .filter(Objects::nonNull)
                .doOnNext(u -> {throw new BadRequest400Exception(USER_ALREADY_EXISTS);})
                .switchIfEmpty(fromCallable(() -> buildUserFrom(request)))
                .doOnNext(userRepository::save)
                .map(User::getId)
                .toBlocking()
                .subscribe(id -> log.info("User id is: {}", id),
                           throwCustomExceptionOrElse(USER_CREATION_ERROR));
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
}
