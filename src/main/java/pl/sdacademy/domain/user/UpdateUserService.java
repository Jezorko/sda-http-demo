package pl.sdacademy.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.sdacademy.domain.authorization.AuthorizationFacade;
import pl.sdacademy.domain.shared.DigestUtil;
import pl.sdacademy.domain.user.dto.request.UpdateUserRequest;

import static java.util.Optional.of;
import static pl.sdacademy.domain.shared.ApiStatus.INTERNAL_SERVER_ERROR;
import static pl.sdacademy.domain.shared.RxJavaCommonFunctions.throwCustomExceptionOrElse;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateUserService {

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
        of(request).map(UpdateUserRequest::getUsername)
                   .ifPresent(user::setUsername);
        of(request).map(UpdateUserRequest::getEmail)
                   .ifPresent(user::setEmail);
        of(request).map(UpdateUserRequest::getPassword)
                   .map(digestUtil::generateHashFrom)
                   .ifPresent(user::setPasswordHash);
        return user;
    }
}
