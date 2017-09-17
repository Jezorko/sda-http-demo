package pl.sdacademy.domain.verification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.sdacademy.domain.authorization.AuthorizationFacade;
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception;
import pl.sdacademy.domain.verification.dto.response.GetVerificationCodeResponse;
import rx.Observable;

import java.util.Objects;

import static pl.sdacademy.domain.shared.ApiStatus.VERIFICATION_NOT_FOUND;
import static rx.Observable.error;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetEmailVerificationService {

    private final AuthorizationFacade authorizationFacade;
    private final EmailVerificationCodeRepository verificationCodeRepository;

    public Observable<GetVerificationCodeResponse> getVerificationCode() {
        return authorizationFacade.getByToken()
                                  .map(verificationCodeRepository::findByUserWhereEmailsAreMatching)
                                  .filter(Objects::nonNull)
                                  .switchIfEmpty(error(new NotFound404Exception(VERIFICATION_NOT_FOUND)))
                                  .map(EmailVerificationCode::getValue)
                                  .map(GetVerificationCodeResponse::new);
    }
}
