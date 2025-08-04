package dev.arsalaan.eagle_bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dev.arsalaan.eagle_bank.enums.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class TransactionResponse {

  private Long id;
  @Enumerated(EnumType.STRING)
  private TransactionType transactionType;
  private BigDecimal amount;
  private LocalDateTime createdAt;

}
