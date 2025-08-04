package dev.arsalaan.eagle_bank.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {

  @NotBlank(message = "First name is required")
  private String firstName;

  @NotBlank(message = "Last name is required")
  private String lastName;

  @Email(message = "Email must be valid")
  private String email;

  // @Size(min = 6, message = "Password must be at least 6 characters")
  // private String password;

  @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
  private String phoneNumber;

  @Past(message = "Date of birth must be in the past")
  private LocalDate dateOfBirth;
}
