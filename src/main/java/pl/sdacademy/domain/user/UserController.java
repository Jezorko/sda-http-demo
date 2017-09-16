package pl.sdacademy.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.domain.user.dto.request.CreateUserRequest;
import pl.sdacademy.domain.user.dto.request.UpdateUserRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
class UserController {

    private final CreateUserService createUserService;
    private final UpdateUserService updateUserService;

    @PostMapping
    @ResponseStatus(CREATED)
    void createUser(@RequestBody @Valid @NotNull CreateUserRequest request) {
        log.info("Incoming user creation request: {}", request);
        createUserService.createUser(request);
    }

    @PutMapping
    @ResponseStatus(NO_CONTENT)
    void updateUser(@RequestBody @Valid @NotNull UpdateUserRequest request) {
        updateUserService.updateUser(request);
    }
}
