package server.preonboarding.budgetmanager.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.preonboarding.budgetmanager.domain.Member;
import server.preonboarding.budgetmanager.dto.AuthLoginRequest;
import server.preonboarding.budgetmanager.dto.AuthLoginResponse;
import server.preonboarding.budgetmanager.exception.CustomException;
import server.preonboarding.budgetmanager.exception.ErrorCode;
import server.preonboarding.budgetmanager.repository.MemberRepository;
import server.preonboarding.budgetmanager.security.JwtProvider;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("인증/인가 서비스 mock 테스트")
@ExtendWith(MockitoExtension.class)
class AuthServiceMockTest {

    @InjectMocks
    private AuthService sut;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtProvider jwtProvider;

    @DisplayName("사용자 로그인")
    @Nested
    class Login {

        @DisplayName("입력된 이메일의 회원이 없으면 예외")
        @Test
        void memberNotFoundByEmail_CustomException() {
            // given
            given(memberRepository.findByEmail(anyString()))
                    .willReturn(Optional.empty());
            AuthLoginRequest loginDto = AuthLoginRequest.builder()
                    .email("jeonggoo75@gmail.com")
                    .password("qlalfqjsgh486^^")
                    .build();

            // when, then
            Assertions.assertThatThrownBy(() -> sut.login(loginDto))
                    .isInstanceOf(CustomException.class)
                    .satisfies(e ->
                        assertThat(e).extracting("errorCode").isSameAs(ErrorCode.MEMBER_INFO_WRONG)
                    );
        }

        @DisplayName("패스워드가 다르면 예외")
        @Test
        void wrongPassword_CustomException() {
            // given
            Member member = mock(Member.class);
            given(memberRepository.findByEmail(anyString()))
                    .willReturn(Optional.of(member));
            AuthLoginRequest loginDto = AuthLoginRequest.builder()
                    .email("jeonggoo75@gmail.com")
                    .password("qlalfqjsgh486^^")
                    .build();
            given(passwordEncoder.matches(loginDto.getPassword(), member.getPassword()))
                    .willReturn(false);

            // when, then
            assertThatThrownBy(() -> sut.login(loginDto))
                    .isInstanceOf(CustomException.class)
                    .satisfies(e ->
                            assertThat(e).extracting("errorCode").isSameAs(ErrorCode.MEMBER_INFO_WRONG)
                    );
        }

        @DisplayName("성공 시 엑세스토큰 발급")
        @Test
        void success_issueAccessToken() {
            // given
            Member member = mock(Member.class);
            given(memberRepository.findByEmail(anyString()))
                    .willReturn(Optional.of(member));
            AuthLoginRequest loginRequest = AuthLoginRequest.builder()
                    .email("jeonggoo75@gmail.com")
                    .password("qlalfqjsgh486^^")
                    .build();
            given(passwordEncoder.matches(loginRequest.getPassword(), member.getPassword()))
                    .willReturn(true);
            given(member.getId()).willReturn(1L);

            // when
            AuthLoginResponse loginResponse = sut.login(loginRequest);

            // then
            verify(jwtProvider).createAccessToken(eq(member.getId()));
            assertThat(loginResponse).isNotNull();
        }

    }

}
