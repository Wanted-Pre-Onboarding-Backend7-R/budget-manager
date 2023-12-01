package server.preonboarding.budgetmanager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import server.preonboarding.budgetmanager.exception.CommonErrorResponse;
import server.preonboarding.budgetmanager.exception.CustomException;
import server.preonboarding.budgetmanager.exception.ErrorCode;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthExceptionTranslationFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            ErrorCode errorCode = e.getErrorCode();
            CommonErrorResponse commonErrorResponse = CommonErrorResponse.from(errorCode);
            String responseBody = mapper.writeValueAsString(commonErrorResponse);
            log.error("{}", responseBody);
            response.setStatus(errorCode.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(Encoding.DEFAULT_CHARSET.name());
            response.getWriter().write(responseBody);

        }
    }

}
