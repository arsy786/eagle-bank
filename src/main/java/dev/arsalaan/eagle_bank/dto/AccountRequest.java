package dev.arsalaan.eagle_bank.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountRequest {

  private String accountName;

  @NotBlank(message = "Account type is required")
  private String accountType;
}
