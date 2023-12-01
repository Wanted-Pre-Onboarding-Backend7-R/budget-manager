package server.preonboarding.budgetmanager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import server.preonboarding.budgetmanager.domain.type.BudgetCategoryType;
import server.preonboarding.budgetmanager.exception.ErrorCode;
import server.preonboarding.budgetmanager.security.JwtAuthenticationFilter;
import server.preonboarding.budgetmanager.security.JwtProvider;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("예산 시나리오 테스트")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class BudgetControllerTest extends AbstractControllerWithAuthTest {

    private static final String BASE_URI = "/api/budgets";

    @Autowired
    private JwtProvider jwtProvider;
    @Value("${jwt.effective-time-millis}")
    private long effectiveTimeMillis;

    @Autowired
    public BudgetControllerTest(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper);
    }

    @DisplayName("카테고리 조회")
    @Nested
    class GetCategories {

        private static final String URI = BASE_URI + "/categories";

        @DisplayName("인가 헤더가 없으면 401")
        @Test
        void withoutAuthHeader_401() throws Exception {
            ErrorCode errorCode = ErrorCode.AUTH_HEADER_NOT_FOUND;
            ResultActions resultActions = mockMvc
                    .perform(get(URI)
                            .accept(MediaType.APPLICATION_JSON)
                    );
            requestAPI(resultActions, errorCode);
        }

        @DisplayName("토큰 타입이 유효하지 않으면 401")
        @Test
        void unmatchedTokenType_401() throws Exception {
            ErrorCode errorCode = ErrorCode.AUTH_TYPE_UNMATCHED;
            ResultActions resultActions = mockMvc
                    .perform(get(URI)
                            .header(HttpHeaders.AUTHORIZATION, "WrongType some-token-value")
                            .accept(MediaType.APPLICATION_JSON)
                    );
            requestAPI(resultActions, errorCode);
        }

        @DisplayName("엑세스 토큰의 형식이 유효하지 않으면 401")
        @Test
        void invalidAccessToken_401() throws Exception {
            ErrorCode errorCode = ErrorCode.AUTH_TOKEN_INVALID;
            ResultActions resultActions = mockMvc
                    .perform(get(URI)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token")
                            .accept(MediaType.APPLICATION_JSON)
                    );
            requestAPI(resultActions, errorCode);
        }

        @DisplayName("엑세스 토큰이 만료되었으면 401")
        @Test
        void expiredAccessToken_401() throws Exception {
            succeedToJoin(EMAIL, PASSWORD);

            ErrorCode errorCode = ErrorCode.AUTH_TOKEN_INVALID;
            Date issuedAt = new Date(System.currentTimeMillis() - (effectiveTimeMillis + 1));
            String accessToken = jwtProvider.createAccessToken(1L, issuedAt);
            String bearerToken = "Bearer " + accessToken;

            ResultActions resultActions = mockMvc
                    .perform(get(URI)
                            .header(HttpHeaders.AUTHORIZATION, bearerToken)
                            .accept(MediaType.APPLICATION_JSON)
                    );
            requestAPI(resultActions, errorCode);
        }

        private void requestAPI(ResultActions resultActions, ErrorCode errorCode) throws Exception {
            resultActions
                    .andExpect(status().is(errorCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(errorCode.name()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()))
                    .andReturn();
        }

        @DisplayName("성공")
        @Test
        void success() throws Exception {
            // given
            String accessToken = joinAndLogin(EMAIL, PASSWORD);
            String bearerToken = JwtAuthenticationFilter.BEARER_TOKEN_PREFIX + accessToken;

            // when
            MvcResult result = mockMvc.perform(get(URI)
                            .header(HttpHeaders.AUTHORIZATION, bearerToken)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andReturn();

            // then
            String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
            List<String> actualCategories = mapper.readValue(responseBody, new TypeReference<List<String>>() {});
            List<String> expectedCategories = Arrays.stream(BudgetCategoryType.values())
                    .map(BudgetCategoryType::getDesc)
                    .toList();
            assertThat(actualCategories).isEqualTo(expectedCategories);
        }

    }

}
