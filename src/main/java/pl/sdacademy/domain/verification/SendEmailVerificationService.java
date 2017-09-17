package pl.sdacademy.domain.verification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.sdacademy.domain.authorization.AuthorizationFacade;
import pl.sdacademy.domain.shared.EmailSendingService;
import pl.sdacademy.domain.user.User;

import javax.transaction.Transactional;

import static pl.sdacademy.domain.shared.ApiStatus.INTERNAL_SERVER_ERROR;
import static pl.sdacademy.domain.shared.EmailSubject.VERIFICATION_CODE;
import static pl.sdacademy.domain.shared.RxJavaCommonFunctions.throwCustomExceptionOrElse;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailVerificationService {

    private final AuthorizationFacade authorizationFacade;
    private final EmailSendingService emailSendingService;
    private final EmailVerificationCodeRepository verificationCodeRepository;
    private final VerificationCodeGenerator verificationCodeGenerator;

    @Transactional
    public void sendEmailVerification() {
        authorizationFacade.getByToken()
                           .doOnNext(verificationCodeRepository::deleteByUser)
                           .map(this::generateCode)
                           .map(verificationCodeRepository::save)
                           .toBlocking()
                           .subscribe(this::send,
                                      throwCustomExceptionOrElse(INTERNAL_SERVER_ERROR));

    }

    private EmailVerificationCode generateCode(User user) {
        return EmailVerificationCode.builder()
                                    .user(user)
                                    .email(user.getEmail())
                                    .value(verificationCodeGenerator.generateEmailVerificationCode())
                                    .verified(false)
                                    .build();
    }

    private void send(EmailVerificationCode code) {
        emailSendingService.sendSimpleMessage(code.getEmail(),
                                              VERIFICATION_CODE,
                                              code.getValue());
    }
}
