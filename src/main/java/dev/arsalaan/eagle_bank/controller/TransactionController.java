package dev.arsalaan.eagle_bank.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.arsalaan.eagle_bank.dto.TransactionRequest;
import dev.arsalaan.eagle_bank.model.Transaction;
import dev.arsalaan.eagle_bank.security.JwtTokenUtil;
import dev.arsalaan.eagle_bank.service.TransactionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/accounts/{accountId}/transactions")
public class TransactionController {

  private final TransactionService transactionService;
  private final JwtTokenUtil jwtTokenUtil;

  public TransactionController(TransactionService transactionService, JwtTokenUtil jwtTokenUtil) {
    this.transactionService = transactionService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @GetMapping("/")
  public ResponseEntity<List<Transaction>> getAllTransactionsByAccountId(
      @PathVariable Long accountId,
      @RequestHeader("Authorization") String authHeader) {

    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);
    List<Transaction> transactions = transactionService.getAllTransactionsByAccountId(accountId, token);
    return ResponseEntity.ok(transactions);
  }

  @GetMapping("/{transactionId}")
  public ResponseEntity<Transaction> getTransactionById(
      @PathVariable Long accountId,
      @PathVariable Long transactionId,
      @RequestHeader("Authorization") String authHeader) {

    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);
    Transaction transaction = transactionService.getTransactionById(accountId, transactionId, token);
    return ResponseEntity.ok(transaction);
  }

  @PostMapping("/")
  public ResponseEntity<Transaction> createTransaction(
      @PathVariable Long accountId,
      @Valid @RequestBody TransactionRequest transactionRequest,
      @RequestHeader("Authorization") String authHeader) {

    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);
    Transaction transaction = transactionService.createTransaction(accountId, transactionRequest, token);
    return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
  }
}