package pl.sdacademy.domain.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.domain.authorization.dto.request.LoginUserRequest;
import pl.sdacademy.domain.authorization.dto.response.LoginUserResponse;
import rx.Observable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/authorizations")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PutMapping
    @ResponseStatus(OK)
    public Observable<LoginUserResponse> login(@RequestBody @Valid @NotNull LoginUserRequest request) {
        log.info("Incoming user login request: {}", request);
        return authorizationService.login(request);
    }
}
