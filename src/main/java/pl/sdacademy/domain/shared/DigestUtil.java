package pl.sdacademy.domain.shared;

import org.springframework.stereotype.Component;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Component
public class DigestUtil {
    public String generateHashFrom(String password) {
        return sha256Hex(password);
    }
}
