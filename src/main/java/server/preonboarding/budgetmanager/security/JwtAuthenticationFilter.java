package server.preonboarding.budgetmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatchers;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import server.preonboarding.budgetmanager.domain.Member;
import server.preonboarding.budgetmanager.exception.CustomException;
import server.preonboarding.budgetmanager.exception.ErrorCode;
import server.preonboarding.budgetmanager.service.MemberService;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String BEARER_TOKEN_PREFIX = "Bearer ";
    public static final RequestMatcher PERMIT_ALL_REQUEST_MATCHER = RequestMatchers.anyOf(
            new AntPathRequestMatcher("/api/members", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/api/auth/login", HttpMethod.POST.name())
    );

    private final JwtDecoder jwtDecoder;
    private final MemberService memberService;
    private final Cache authCache = new ConcurrentMapCache("auth");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = extractAccessToken(request);
        Authentication auth = getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PERMIT_ALL_REQUEST_MATCHER.matches(request);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return parseAccessToken(header);
    }

    private static String parseAccessToken(String header) {
        verifyIfAuthHeaderExists(header);
        verifyIfGrantTypeIsBearer(header);
        return header.substring(BEARER_TOKEN_PREFIX.length());
    }

    private static void verifyIfAuthHeaderExists(String header) {
        if (!StringUtils.hasText(header)) {
            throw new CustomException(ErrorCode.AUTH_HEADER_NOT_FOUND);
        }
    }

    private static void verifyIfGrantTypeIsBearer(String header) {
        if (!header.startsWith(BEARER_TOKEN_PREFIX)) {
            throw new CustomException(ErrorCode.AUTH_TYPE_UNMATCHED);
        }
    }

    private Authentication getAuthentication(String accessToken) {
        Long id = getId(accessToken);
        Authentication auth = authCache.get(id, Authentication.class);
        if (auth != null) {
            return auth;
        }
        Member member = memberService.getMember(id);
        auth = JwtAuthenticationToken.authenticated(member, id);
        authCache.put(id, auth);
        return auth;
    }

    private Long getId(String accessToken) {
        try {
            return jwtDecoder.extractSubject(accessToken);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AUTH_TOKEN_INVALID, e);
        }
    }

}
