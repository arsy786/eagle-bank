package dev.arsalaan.eagle_bank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

import dev.arsalaan.eagle_bank.enums.TransactionType;

@Data
public class TransactionRequest {

  @NotNull(message = "Transaction type is required")
  private TransactionType transactionType;

  @NotNull(message = "Amount is required")
  @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
  private BigDecimal amount;
}