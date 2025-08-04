package dev.arsalaan.eagle_bank.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserResponse {

  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private LocalDate dateOfBirth;

}
