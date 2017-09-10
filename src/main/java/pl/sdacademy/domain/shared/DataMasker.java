package pl.sdacademy.domain.shared;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

import static java.util.Objects.*;
import static org.apache.commons.codec.binary.Hex.encodeHex;
import static org.apache.commons.lang3.StringUtils.repeat;

@Slf4j
public class DataMasker {

    public static String mask(CharSequence dataToMask) {
        if (isNull(dataToMask)) {
            return "null";
        }
        else {
            return repeat('*', dataToMask.length());
        }
    }

    public static String shortHash(String dataToMask) {
        if (isNull(dataToMask)) {
            return "null";
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(dataToMask.getBytes("UTF-8"));
            byte[] result = digest.digest();
            return new String(encodeHex(result)).substring(0, 5);
        } catch (Exception e) {
            log.info("Hashing failure, fallback to masking: {}", e);
            return mask(dataToMask);
        }
    }

}
