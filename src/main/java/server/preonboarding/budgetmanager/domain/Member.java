package server.preonboarding.budgetmanager.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseIdEntity {

    private static final Collection<? extends GrantedAuthority> USER_AUTHORITY = List.of(
            new SimpleGrantedAuthority("USER")
    );

    @Column(length = 40, unique = true, nullable = false)
    private String email;
    @Column(length = 60, nullable = false)
    private String password;

    @Builder
    private Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return USER_AUTHORITY;
    }

}
