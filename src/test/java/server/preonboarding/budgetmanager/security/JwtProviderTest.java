package server.preonboarding.budgetmanager.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtProviderTest {

    private final JwtProvider jwtProvider = new JwtProvider("c2VjcmV0LXNlY3JldC1zZWNyZXQtc2VjcmV0LXNlY3JldA==");

    @DisplayName("JwtProvider 생성 후 subject 추출")
    @Test
    void createAccessTokenAndExtractSubject() {
        String accessToken = jwtProvider.createAccessToken(1L);
        Long id = jwtProvider.extractSubject(accessToken);
        Assertions.assertThat(id).isEqualTo(1L);
    }

}
