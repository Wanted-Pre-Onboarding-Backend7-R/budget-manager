package server.preonboarding.budgetmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class AuthLoginRequest {

    @NotBlank(message = "EMAIL_BLANK")
    @Email(message = "EMAIL_INVALID_FORMAT",
            regexp = "^\\S+@\\S+\\.\\S+$",
            groups = Email.class)
    private final String email;

    @NotBlank(message = "PASSWORD_BLANK")
    @Pattern(message = "PASSWORD_INVALID_FORMAT",
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&^])[A-Za-z\\d@$!%*#?&^]{8,24}$",
            groups = Pattern.class)
    private final String password;

    @Builder
    private AuthLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
