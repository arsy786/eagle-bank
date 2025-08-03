package dev.arsalaan.eagle_bank.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountResponse {

  private Long id;
  private String accountNumber;
  private String accountName;
  private String accountType;
  private BigDecimal balance;
  private LocalDateTime createdAt;
}
