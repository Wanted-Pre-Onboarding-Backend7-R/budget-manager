package server.preonboarding.budgetmanager.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        byte[] bytes = Decoders.BASE64.decode(secret);
        secretKey = Keys.hmacShaKeyFor(bytes);
        jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public String createAccessToken(Long id) {
        Date now = new Date();
        return Jwts.builder()
                .subject(id.toString())
                .issuedAt(now)
                .signWith(secretKey)
                .compact();
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
