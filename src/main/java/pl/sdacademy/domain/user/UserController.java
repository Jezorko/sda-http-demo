package pl.sdacademy.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.domain.user.dto.request.CreateUserRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
class UserController {

    private final CreateUserService createUserService;

    @PostMapping
    @ResponseStatus(CREATED)
    void createUser(@RequestBody @Valid @NotNull CreateUserRequest request) {
        log.info("Incoming user creation request: {}", request);
        createUserService.createUser(request);
    }
}
