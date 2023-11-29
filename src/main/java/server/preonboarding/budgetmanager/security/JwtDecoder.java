package server.preonboarding.budgetmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class JwtDecoder {

    private final JwtParser jwtParser;

    public JwtDecoder(SecretKey secretKey) {
        jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public Long extractSubject(String accessToken) {
        String id = getJwsClaims(accessToken)
                .getPayload()
                .getSubject();
        return Long.parseLong(id);
    }

    private Jws<Claims> getJwsClaims(String accessToken) {
        return jwtParser.parseSignedClaims(accessToken);
    }

}
