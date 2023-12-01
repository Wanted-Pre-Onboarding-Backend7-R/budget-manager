package server.preonboarding.budgetmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import server.preonboarding.budgetmanager.dto.AuthLoginRequest;
import server.preonboarding.budgetmanager.dto.MemberJoinRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

abstract class AbstractControllerWithAuthTest {

    protected static final String EMAIL = "jeonggoo75@gmail.com";
    protected static final String PASSWORD = "qlalfqjsgh486^^";

    protected final MockMvc mockMvc;
    protected final ObjectMapper mapper;

    public AbstractControllerWithAuthTest(MockMvc mockMvc, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
    }

    protected String joinAndLogin(String email, String password) throws Exception {
        succeedToJoin(email, password);
        return succeedToLogin(email, password);
    }

    protected void succeedToJoin(String email, String password) throws Exception {
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

    protected String succeedToLogin(String email, String password) throws Exception {
        AuthLoginRequest loginDto = AuthLoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String loginContent = mapper.writeValueAsString(loginDto);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        return mapper.readTree(responseBody).get("accessToken").asText();
    }

}
