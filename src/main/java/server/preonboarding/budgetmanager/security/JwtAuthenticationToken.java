package server.preonboarding.budgetmanager.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import server.preonboarding.budgetmanager.domain.Member;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Member principal;
    private final Long credentials;

    private JwtAuthenticationToken(Member member, Long id) {
        super(member.getAuthorities());
        principal = member;
        credentials = id;
        super.setAuthenticated(true);
    }

    public static JwtAuthenticationToken authenticated(Member member, Long id) {
        return new JwtAuthenticationToken(member, id);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new UnsupportedOperationException("Cannot change authentication status.");
    }

}
