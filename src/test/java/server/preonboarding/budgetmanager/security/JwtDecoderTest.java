package server.preonboarding.budgetmanager.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SecurityConfig.class)
class JwtDecoderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtDecoder jwtDecoder;

    @DisplayName("JWT 액세스 토큰 생성 후 subject 추출")
    @Test
    void createAccessTokenAndExtractSubject() {
        String accessToken = jwtProvider.createAccessToken(1L);
        Long id = jwtDecoder.extractSubject(accessToken);
        Assertions.assertThat(id).isEqualTo(1L);
    }

}
