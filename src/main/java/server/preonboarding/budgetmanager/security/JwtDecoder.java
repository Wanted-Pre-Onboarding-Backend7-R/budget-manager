package server.preonboarding.budgetmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import server.preonboarding.budgetmanager.exception.CustomException;
import server.preonboarding.budgetmanager.exception.ErrorCode;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtDecoder {

    private final JwtParser jwtParser;
    private final long effectiveTimeMillis;

    public JwtDecoder(SecretKey secretKey, long effectiveTimeMillis) {
        jwtParser = Jwts.parser().verifyWith(secretKey).build();
        this.effectiveTimeMillis = effectiveTimeMillis;
    }

    public Long extractSubject(String accessToken) {
        Claims payload = getPayload(accessToken);
        verifyIfTokenIsEffective(payload);
        return Long.parseLong(payload.getSubject());
    }

    private void verifyIfTokenIsEffective(Claims payload) {
        Date issuedAt = payload.getIssuedAt();
        Date now = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + effectiveTimeMillis);
        boolean isExpiredToken = now.after(expiredAt);
        if (isExpiredToken) {
            throw new CustomException(ErrorCode.AUTH_TOKEN_EXPIRED);
        }
    }

    private Claims getPayload(String accessToken) {
        return jwtParser.parseSignedClaims(accessToken).getPayload();
    }

}
