package pl.sdacademy.domain.verification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.domain.verification.dto.request.ConfirmEmailVerificationRequest;
import pl.sdacademy.domain.verification.dto.response.GetVerificationCodeResponse;
import rx.Observable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/emails/verifications")
public class EmailVerificationController {

    private final GetEmailVerificationService getEmailVerificationService;
    private final SendEmailVerificationService sendEmailVerificationService;
    private final ConfirmEmailVerificationService confirmEmailVerificationService;

    @GetMapping
    @ResponseStatus(OK)
    Observable<GetVerificationCodeResponse> getEmailVerification() {
        log.info("Incoming user email verification query request");
        return getEmailVerificationService.getVerificationCode();
    }

    @PostMapping
    @ResponseStatus(NO_CONTENT)
    void sendEmailVerification() {
        log.info("Incoming user email verification request");
        sendEmailVerificationService.sendEmailVerification();
    }

    @PutMapping
    @ResponseStatus(NO_CONTENT)
    void confirmEmailVerification(@RequestBody @Valid @NotNull ConfirmEmailVerificationRequest request) {
        log.info("Incoming user email verification confirmation request");
        confirmEmailVerificationService.confirmEmailVerification(request);
    }
}
