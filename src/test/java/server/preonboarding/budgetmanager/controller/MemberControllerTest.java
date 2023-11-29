package server.preonboarding.budgetmanager.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("/api/members 통합 테스트")
@AutoConfigureMockMvc
@SpringBootTest
public class MemberControllerTest {

    private static final String BASE_URI = "/api/members";

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("사용자 회원가입")
    @Nested
    class Join {

        private static final String ACCOUNT_INFO_JSON_FORM = "{\"email\":\"%s\",\"password\":\"%s\"}";
        private static final String URI = BASE_URI;

        @DisplayName("성공")
        @Test
        void pass() throws Exception {
            String requestBody = ACCOUNT_INFO_JSON_FORM.formatted("jeonggoo75@gmail.com", "qlalfqjsgh486^^");

            mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$").doesNotExist())
                    .andDo(print())
                    .andReturn();
        }

    }

}
