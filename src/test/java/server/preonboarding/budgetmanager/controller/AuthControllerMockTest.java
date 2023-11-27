package server.preonboarding.budgetmanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import server.preonboarding.budgetmanager.dto.AuthLoginRequest;
import server.preonboarding.budgetmanager.exception.ErrorCode;
import server.preonboarding.budgetmanager.service.AuthService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("/api/auth WebMvc 테스트")
@MockBean(AuthService.class)
@Import(TestSecurityConfig.class)
@WebMvcTest(AuthController.class)
class AuthControllerMockTest {

    private static final String BASE_URI = "/api/auth";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @DisplayName("사용자 로그인")
    @Nested
    class Login {

        private static final String URI = BASE_URI + "/login";

        @DisplayName("이메일 비었을 때")
        @Test
        void blankEmail_badRequest() throws Exception {
            String content = makeContent("", "qlalfqjsgh486^^");
            performMockMvc(ErrorCode.EMAIL_BLANK, content);
        }

        @DisplayName("비밀번호 비었을 때")
        @Test
        void blankPassword_badRequest() throws Exception {
            String content = makeContent("jeonggoo75@gmail.com", "");
            performMockMvc(ErrorCode.PASSWORD_BLANK, content);
        }

        private void performMockMvc(ErrorCode errorCode, String content) throws Exception {
            mockMvc.perform(post(URI)
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.errorCode").value(errorCode.name()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()))
                    .andDo(print());
        }

        private String makeContent(String email, String password) throws JsonProcessingException {
            AuthLoginRequest dto = AuthLoginRequest.builder()
                    .email(email)
                    .password(password)
                    .build();
            return mapper.writeValueAsString(dto);
        }

    }

}
