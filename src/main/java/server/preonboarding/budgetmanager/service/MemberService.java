package server.preonboarding.budgetmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.preonboarding.budgetmanager.MemberRepository;
import server.preonboarding.budgetmanager.domain.Member;
import server.preonboarding.budgetmanager.dto.MemberJoinRequest;
import server.preonboarding.budgetmanager.exception.CustomException;
import server.preonboarding.budgetmanager.exception.ErrorCode;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public Long join(MemberJoinRequest dto) {
        String encodedPw = encodePassword(dto);
        Member member = Member.builder()
                .email(dto.getEmail())
                .password(encodedPw)
                .build();
        try {
            Member savedMember = memberRepository.save(member);
            return savedMember.getId();
        } catch (RuntimeException e) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATE);
        }
    }

    private String encodePassword(MemberJoinRequest dto) {
        return passwordEncoder.encode(dto.getPassword());
    }

}
