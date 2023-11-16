package server.preonboarding.budgetmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberJoinRequest {

    @NotBlank(message = "EMAIL_BLANK")
    @Email(regexp = "^\\S+@\\S+\\.\\S+$", message = "EMAIL_INVALID_FORMAT", groups = Email.class)
    private String email;

    @NotBlank(message = "PASSWORD_BLANK")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&^])[A-Za-z\\d@$!%*#?&^]{8,24}$",
            message = "PASSWORD_INVALID_FORMAT",
            groups = Pattern.class)
    private String password;


    @Builder
    private MemberJoinRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
