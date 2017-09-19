package pl.sdacademy.domain.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sdacademy.domain.user.User;

import static java.lang.Boolean.FALSE;
import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class EmailVerificationFacade {
    private final EmailVerificationCodeRepository verificationCodeRepository;

    public boolean hasVerifiedEmail(User user) {
        return ofNullable(user).map(verificationCodeRepository::getByUserWhereEmailsAreMatching)
                               .map(EmailVerificationCode::getVerified)
                               .orElse(FALSE);
    }

}
