package by.bsuir.karamach.serviceworker.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;

@Component
public class SecurityHelper {
    private static final int TOKEN_LENGTH = 20;
    public static final int MIN_RANGE = 100000;
    public static final int MAX_RANGE = 999999;


    private SecureRandom random = new SecureRandom();
    private Algorithm algorithm = Algorithm.HMAC256("secret-key");

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

    public String generateJWTToken(Map<String, String> values) {

        JWTCreator.Builder jwtToken = JWT.create().withIssuer("myauth10");


        for (Map.Entry<String, String> param : values.entrySet()) {

            jwtToken = jwtToken.withClaim(param.getKey(), param.getValue());

        }


        String token = jwtToken.sign(algorithm);

        return token;
    }

}
