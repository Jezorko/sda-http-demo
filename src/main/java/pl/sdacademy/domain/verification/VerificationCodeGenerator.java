package pl.sdacademy.domain.verification;

import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Component
public class VerificationCodeGenerator {
    String generateEmailVerificationCode() {
        return randomNumeric(5);
    }
}
