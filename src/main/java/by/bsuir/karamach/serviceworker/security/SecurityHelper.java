package by.bsuir.karamach.serviceworker.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

@Component
public class SecurityHelper {
    private static final int TOKEN_LENGTH = 20;
    public static final int MIN_RANGE = 100000;
    public static final int MAX_RANGE = 999999;


    private SecureRandom random = new SecureRandom();

    public String generateTempToken() {
        byte bytes[] = new byte[TOKEN_LENGTH];
        random.nextBytes(bytes);
        String token = bytes.toString();

        return token;
    }

    public String generateActivationCode() {
        int result = MIN_RANGE + (int) (Math.random() * MAX_RANGE);

        return String.valueOf(result);
    }

    public String generatePublicId() {
        return UUID.randomUUID().toString();
    }
}
