package server.preonboarding.budgetmanager.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseIdEntity {

    @Column(length = 40, unique = true, nullable = false)
    private String email;
    @Column(length = 60, nullable = false)
    private String password;

    @Builder
    private Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
