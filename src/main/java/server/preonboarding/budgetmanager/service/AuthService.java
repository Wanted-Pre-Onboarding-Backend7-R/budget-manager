package server.preonboarding.budgetmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.preonboarding.budgetmanager.repository.MemberRepository;
import server.preonboarding.budgetmanager.domain.Member;
import server.preonboarding.budgetmanager.dto.AuthLoginRequest;
import server.preonboarding.budgetmanager.dto.AuthLoginResponse;
import server.preonboarding.budgetmanager.exception.CustomException;
import server.preonboarding.budgetmanager.exception.ErrorCode;
import server.preonboarding.budgetmanager.security.JwtProvider;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public AuthLoginResponse login(AuthLoginRequest dto) {
        Member member = getMember(dto);
        verifyPassword(dto, member);
        String accessToken = jwtProvider.createAccessToken(member.getId());
        return new AuthLoginResponse(accessToken);
    }

    private Member getMember(AuthLoginRequest dto) {
        return memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_INFO_WRONG));
    }

    private void verifyPassword(AuthLoginRequest dto, Member member) {
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.MEMBER_INFO_WRONG);
        }
    }

}
