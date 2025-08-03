package dev.arsalaan.eagle_bank.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.arsalaan.eagle_bank.dto.AccountRequest;
import dev.arsalaan.eagle_bank.dto.AccountResponse;
import dev.arsalaan.eagle_bank.security.JwtTokenUtil;
import dev.arsalaan.eagle_bank.service.AccountService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

  private final AccountService accountService;
  private final JwtTokenUtil jwtTokenUtil;

  public AccountController(AccountService accountService, JwtTokenUtil jwtTokenUtil) {
    this.accountService = accountService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @GetMapping
  public ResponseEntity<List<AccountResponse>> getAllAccounts(@RequestHeader("Authorization") String authHeader) {
    log.info("GET /v1/accounts called");

    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);
    List<AccountResponse> accounts = accountService.getAllAccounts(token);

    log.info("Retrieved {} accounts", accounts.size());
    return ResponseEntity.ok(accounts);
  }

  @GetMapping("/{accountId}")
  public ResponseEntity<AccountResponse> getAccountById(
      @PathVariable Long accountId,
      @RequestHeader("Authorization") String authHeader) {

    log.info("GET /v1/accounts/{} called", accountId);

    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);
    AccountResponse account = accountService.getAccountById(accountId, token);

    log.info("Retrieved account with id {}", accountId);
    return ResponseEntity.ok(account);
  }

  @PostMapping
  public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest,
      @RequestHeader("Authorization") String authHeader) {

    log.info("POST /v1/accounts called");

    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);

    AccountResponse account = accountService.createAccount(accountRequest, token);

    log.info("Account created successfully");
    return ResponseEntity.status(HttpStatus.CREATED).body(account);
  }

  @PatchMapping("/{accountId}")
  public ResponseEntity<AccountResponse> updateAccountById(
      @PathVariable Long accountId,
      @RequestBody AccountRequest accountRequest,
      @RequestHeader("Authorization") String authHeader) {

    log.info("PATCH /v1/accounts/{} called", accountId);

    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);
    AccountResponse updatedAccount = accountService.updateAccountById(accountId, accountRequest, token);

    log.info("Account updated successfully");
    return ResponseEntity.ok(updatedAccount);
  }

  @DeleteMapping("/{accountId}")
  public ResponseEntity<String> deleteAccountById(
      @PathVariable Long accountId,
      @RequestHeader("Authorization") String authHeader) {

    log.info("DELETE /v1/accounts/{} called", accountId);

    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);
    accountService.deleteAccountById(accountId, token);

    log.info("Account deleted successfully");
    return ResponseEntity.ok("Account deleted successfully");
  }
}
