package server.preonboarding.budgetmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import server.preonboarding.budgetmanager.dto.AuthLoginRequest;
import server.preonboarding.budgetmanager.exception.ErrorCode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("로그인 시나리오 테스트")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest extends AbstractControllerWithAuthTest {

    @Autowired
    public AuthControllerTest(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper);
    }

    @DisplayName("회원가입 후 잘못된 이메일 입력으로 로그인 실패")
    @Test
    void wrongEmail_badRequest() throws Exception {
        succeedToJoin(EMAIL, PASSWORD);

        String wrongEmail = "wrong_email@gmail.com";
        failToLogin(wrongEmail, PASSWORD);
    }

    @DisplayName("회원가입 후 잘못된 비밀번호 입력으로 로그인 실패")
    @Test
    void wrongPassword_badRequest() throws Exception {
        succeedToJoin(EMAIL, PASSWORD);

        String wrongPassword = "wrong_password";
        failToLogin(EMAIL, wrongPassword);
    }

    @DisplayName("회원가입 후 로그인 성공")
    @Test
    void succeedToLogin_Ok() throws Exception {
        succeedToJoin(EMAIL, PASSWORD);
        succeedToLogin(EMAIL, PASSWORD);
    }

    private void failToLogin(String email, String password) throws Exception {
        ErrorCode errorCode = ErrorCode.MEMBER_INFO_WRONG;
        AuthLoginRequest loginDto = AuthLoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String loginContent = mapper.writeValueAsString(loginDto);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(errorCode.name()))
                .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
    }

}
