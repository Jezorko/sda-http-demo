package pl.sdacademy.domain.verification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.sdacademy.domain.authorization.AuthorizationFacade;
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception;
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception;
import pl.sdacademy.domain.verification.dto.request.ConfirmEmailVerificationRequest;

import java.util.Objects;

import static pl.sdacademy.domain.shared.ApiStatus.*;
import static pl.sdacademy.domain.shared.RxJavaCommonFunctions.throwCustomExceptionOrElse;
import static rx.Observable.error;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmEmailVerificationService {

    private final AuthorizationFacade authorizationFacade;
    private final EmailVerificationCodeRepository verificationCodeRepository;

    public void confirmEmailVerification(ConfirmEmailVerificationRequest request) {
        authorizationFacade.getByToken()
                           .map(verificationCodeRepository::getByUserWhereEmailsAreMatching)
                           .filter(Objects::nonNull)
                           .switchIfEmpty(error(new NotFound404Exception(VERIFICATION_NOT_FOUND)))
                           .filter(code -> code.getValue()
                                               .equals(request.getVerificationCode()))
                           .switchIfEmpty(error(new BadRequest400Exception(INVALID_VERIFICATION_CODE)))
                           .doOnNext(code -> code.setVerified(true))
                           .toBlocking()
                           .subscribe(verificationCodeRepository::save,
                                      throwCustomExceptionOrElse(INTERNAL_SERVER_ERROR));
    }
}
