package server.preonboarding.budgetmanager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.preonboarding.budgetmanager.service.MemberService;

import javax.crypto.SecretKey;

@Configuration
public class JwtAuthConfig {

    @Bean
    public SecretKey secretKey(@Value("${jwt.secret}") String secret) {
        byte[] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }

    @Bean
    public JwtProvider jwtProvider(SecretKey secretKey) {
        return new JwtProvider(secretKey);
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey secretKey, @Value("${jwt.effective-time-millis}") long effectiveTimeMillis) {
        return new JwtDecoder(secretKey, effectiveTimeMillis);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtDecoder jwtDecoder, MemberService memberService) {
        return new JwtAuthenticationFilter(jwtDecoder, memberService);
    }

    @Bean
    public JwtAuthExceptionTranslationFilter jwtAuthExceptionTranslationFilter(ObjectMapper mapper) {
        return new JwtAuthExceptionTranslationFilter(mapper);
    }

}
