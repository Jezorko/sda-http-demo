package pl.sdacademy.domain.shared;

import org.springframework.stereotype.Component;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Component
public class DigestUtil {

    private static final String RANDOM_SALT = randomAlphanumeric(10);

    public String generateHashFrom(String password) {
        return sha256Hex(sha256Hex(password) + RANDOM_SALT);
    }
}
