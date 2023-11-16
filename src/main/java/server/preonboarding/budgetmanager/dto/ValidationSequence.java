package server.preonboarding.budgetmanager.dto;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;

@GroupSequence({Default.class, NotBlank.class, Email.class, Pattern.class})
public interface ValidationSequence {
}
