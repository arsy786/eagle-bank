package dev.arsalaan.eagle_bank.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import dev.arsalaan.eagle_bank.dto.TransactionRequest;
import dev.arsalaan.eagle_bank.exception.ApiRequestException;
import dev.arsalaan.eagle_bank.model.Account;
import dev.arsalaan.eagle_bank.model.Transaction;
import dev.arsalaan.eagle_bank.enums.TransactionType;

import dev.arsalaan.eagle_bank.repository.AccountRepository;
import dev.arsalaan.eagle_bank.repository.TransactionRepository;
import dev.arsalaan.eagle_bank.security.JwtTokenUtil;

@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;
  private final JwtTokenUtil jwtTokenUtil;

  public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository,
      JwtTokenUtil jwtTokenUtil) {
    this.transactionRepository = transactionRepository;
    this.accountRepository = accountRepository;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  public Transaction createTransaction(Long accountId, TransactionRequest transactionRequest, String token) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ApiRequestException(HttpStatus.NOT_FOUND, "Account not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!account.getUser().getEmail().equals(email)) {
      throw new ApiRequestException(HttpStatus.FORBIDDEN, "You are not authorized to access this account");
    }

    TransactionType transactionType = transactionRequest.getTransactionType();

    if (transactionType == TransactionType.WITHDRAWAL &&
        account.getBalance().compareTo(transactionRequest.getAmount()) < 0) {
      throw new ApiRequestException(HttpStatus.UNPROCESSABLE_ENTITY, "Insufficient funds");
    }

    Transaction transaction = Transaction.builder()
        .transactionType(transactionType)
        .amount(transactionRequest.getAmount())
        .createdAt(LocalDateTime.now())
        .account(account)
        .build();

    if (transactionType == TransactionType.DEPOSIT) {
      account.setBalance(account.getBalance().add(transactionRequest.getAmount()));
    } else {
      account.setBalance(account.getBalance().subtract(transactionRequest.getAmount()));
    }

    accountRepository.save(account);
    return transactionRepository.save(transaction);
  }

  public List<Transaction> getAllTransactionsByAccountId(Long accountId, String token) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ApiRequestException(HttpStatus.NOT_FOUND, "Account not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!account.getUser().getEmail().equals(email)) {
      throw new ApiRequestException(HttpStatus.FORBIDDEN, "You are not authorized to access this account");
    }

    return transactionRepository.findByAccountId(accountId);
  }

  public Transaction getTransactionById(Long accountId, Long transactionId, String token) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ApiRequestException(HttpStatus.NOT_FOUND, "Account not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!account.getUser().getEmail().equals(email)) {
      throw new ApiRequestException(HttpStatus.FORBIDDEN, "You are not authorized to access this account");
    }

    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new ApiRequestException(HttpStatus.NOT_FOUND, "Transaction not found"));

    if (!transaction.getAccount().getId().equals(accountId)) {
      throw new ApiRequestException(HttpStatus.NOT_FOUND, "Transaction not found");
    }

    return transaction;
  }
}