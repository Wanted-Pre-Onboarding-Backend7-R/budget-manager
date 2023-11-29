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
import server.preonboarding.budgetmanager.dto.MemberJoinRequest;
import server.preonboarding.budgetmanager.exception.ErrorCode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @DisplayName("회원가입 후 잘못된 이메일 입력으로 로그인 실패")
    @Test
    void wrongEmail_badRequest() throws Exception {
        String email = "jeonggoo75@gmail.com";
        String password = "qlalfqjsgh486^^";

        succeedToJoin(email, password);

        String wrongEmail = "wrong_email@gmail.com";
        failToLogin(wrongEmail, password);
    }

    @DisplayName("회원가입 후 잘못된 비밀번호 입력으로 로그인 실패")
    @Test
    void wrongPassword_badRequest() throws Exception {
        String email = "jeonggoo75@gmail.com";
        String password = "qlalfqjsgh486^^";

        succeedToJoin(email, password);

        String wrongEmail = "wrong_password";
        failToLogin(wrongEmail, password);
    }

    @DisplayName("회원가입 후 로그인 성공")
    @Test
    void succeedToLogin_Ok() throws Exception {
        String email = "jeonggoo75@gmail.com";
        String password = "qlalfqjsgh486^^";

        succeedToJoin(email, password);
        succeedToLogin(email, password);
    }

    private void succeedToJoin(String email, String password) throws Exception {
        // 회원가입
        MemberJoinRequest joinDto = MemberJoinRequest.builder()
                .email(email)
                .password(password)
                .build();
        String joinContent = mapper.writeValueAsString(joinDto);
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(joinContent)
                )
                .andExpect(status().isCreated());
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

    private void succeedToLogin(String email, String password) throws Exception {
        AuthLoginRequest loginDto = AuthLoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String loginContent = mapper.writeValueAsString(loginDto);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString());
    }

}
