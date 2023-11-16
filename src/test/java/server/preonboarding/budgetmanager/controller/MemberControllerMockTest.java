package server.preonboarding.budgetmanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import server.preonboarding.budgetmanager.exception.CommonErrorResponse;
import server.preonboarding.budgetmanager.exception.ErrorCode;
import server.preonboarding.budgetmanager.service.MemberService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("/api/members WebMvc 테스트")
@MockBean(MemberService.class)
@Import(TestSecurityConfig.class)
@WebMvcTest(MemberController.class)
class MemberControllerMockTest {

    private final String BASE_URI = "/api/members";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @DisplayName("사용자 회원가입")
    @Nested
    class Join {

        private static final String ACCOUNT_INFO_JSON_FORM = "{\"email\":\"%s\",\"password\":\"%s\"}";

        @DisplayName("이메일 비었으면 400")
        @Test
        void emptyEmail_400() throws Exception {
            String accountInfoJson = createAccountInfoJson("", "qlalfqjsgh486^^");

            performMockMvc(accountInfoJson, ErrorCode.EMAIL_BLANK);
        }

        @DisplayName("이메일 유효하지 않은 형식이면 400")
        @ValueSource(strings = {"as", "a.a" ,"a@a", "a@@a.com", "a@.com"})
        @ParameterizedTest
        void invalidEmail_400(String email) throws Exception {
            String accountInfoJson = createAccountInfoJson(email, "qlalfqjsgh486^^");

            performMockMvc(accountInfoJson, ErrorCode.EMAIL_INVALID_FORMAT);
        }

        @DisplayName("패스워드 비었으면 400")
        @Test
        void emptyPassword_400() throws Exception {
            String accountInfoJson = createAccountInfoJson("jeonggoo75@gmail.com", "");

            performMockMvc(accountInfoJson, ErrorCode.PASSWORD_BLANK);
        }

        @DisplayName("이메일 유효하지 않은 형식이면 400")
        @ValueSource(strings = {"password", "asd1283192ads", "123as!!", "123456789ASDFZXCVQWERTYUI!@!@!@^^"})
        @ParameterizedTest
        void invalidPassword_400(String password) throws Exception {
            String accountInfoJson = createAccountInfoJson("jeonggoo75@gmail.com", password);

            performMockMvc(accountInfoJson, ErrorCode.PASSWORD_INVALID_FORMAT);
        }

        @DisplayName("유효한 이메일, 패스워드이면 200")
        @Test
        void validAccountInfo_200() throws Exception {
            String accountInfoJson = createAccountInfoJson("jeonggoo75@gmail.com", "qlalfqjsgh486^^");

            mockMvc.perform(post(BASE_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(accountInfoJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").doesNotExist())
                    .andDo(print())
                    .andReturn();
        }

        private String createAccountInfoJson(String email, String password) {
            return ACCOUNT_INFO_JSON_FORM.formatted(email, password);
        }

        private void performMockMvc(String accountInfoJson, ErrorCode errorCode) throws Exception {
            String responseBody = createResponseBody(errorCode);
            mockMvc.perform(post(BASE_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(accountInfoJson))
                    .andExpect(status().is(errorCode.getHttpStatus().value()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(responseBody))
                    .andDo(print())
                    .andReturn();
        }

        private String createResponseBody(ErrorCode errorCode) throws JsonProcessingException {
            CommonErrorResponse response = CommonErrorResponse.from(errorCode);
            return mapper.writeValueAsString(response);
        }

    }

}
