package server.preonboarding.budgetmanager.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.preonboarding.budgetmanager.domain.Member;
import server.preonboarding.budgetmanager.dto.MemberJoinRequest;
import server.preonboarding.budgetmanager.exception.CustomException;
import server.preonboarding.budgetmanager.exception.ErrorCode;
import server.preonboarding.budgetmanager.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("MemberService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceMockTest {

    @InjectMocks
    private MemberService sut;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("사용자 회원가입")
    @Nested
    class Join {

        @DisplayName("같은 이메일이 존재하면 예외")
        @Test
        void duplicateEmail_exception() {
            // given
            given(memberRepository.save(any(Member.class)))
                    .willThrow(RuntimeException.class);
            given(passwordEncoder.encode(anyString()))
                    .willReturn("encoded_password_length_is_60");

            // when, then
            MemberJoinRequest dto = MemberJoinRequest.builder()
                    .email("jeonggoo75@gmail.com")
                    .password("qlalfqjsgh486^^")
                    .build();
            assertThatThrownBy(() -> sut.join(dto))
                    .isInstanceOf(CustomException.class)
                    .satisfies(e -> {
                        assertThat(e).extracting("errorCode").isSameAs(ErrorCode.EMAIL_DUPLICATE);
                    });
        }

        @DisplayName("성공")
        @Test
        void pass() {
            // given
            Member savedMember = mock(Member.class);
            given(memberRepository.save(any(Member.class)))
                    .willReturn(savedMember);
            given(savedMember.getId())
                    .willReturn(1L);
            given(passwordEncoder.encode(anyString()))
                    .willReturn("encoded_password_length_is_60");

            // when, then
            MemberJoinRequest dto = MemberJoinRequest.builder()
                    .email("jeonggoo75@gmail.com")
                    .password("qlalfqjsgh486^^")
                    .build();

            Long ret = sut.join(dto);
            assertThat(savedMember.getId()).isEqualTo(ret);
        }

    }


}
