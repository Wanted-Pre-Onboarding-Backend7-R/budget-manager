package server.preonboarding.budgetmanager.security;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;

@RequiredArgsConstructor
public class JwtProvider {

    private final SecretKey secretKey;

    public String createAccessToken(Long id) {
        Date now = new Date();
        return createAccessToken(id, now);
    }

    public String createAccessToken(Long id, Date issuedAt) {
        return Jwts.builder()
                .subject(id.toString())
                .issuedAt(issuedAt)
                .signWith(secretKey)
                .compact();
    }

}
