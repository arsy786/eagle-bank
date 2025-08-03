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
import dev.arsalaan.eagle_bank.model.Account;
import dev.arsalaan.eagle_bank.security.JwtTokenUtil;
import dev.arsalaan.eagle_bank.service.AccountService;
import jakarta.validation.Valid;

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
  public ResponseEntity<List<Account>> getAllAccounts(@RequestHeader("Authorization") String authHeader) {
    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);
    List<Account> accounts = accountService.getAllAccounts(token);
    return ResponseEntity.ok(accounts);
  }

  @GetMapping("/{accountId}")
  public ResponseEntity<Account> getAccountById(
      @PathVariable Long accountId,
      @RequestHeader("Authorization") String authHeader) {

    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);
    Account account = accountService.getAccountById(accountId, token);
    return ResponseEntity.ok(account);
  }

  @PostMapping
  public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountRequest accountRequest,
      @RequestHeader("Authorization") String authHeader) {

    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);

    Account account = accountService.createAccount(accountRequest, token);

    return ResponseEntity.status(HttpStatus.CREATED).body(account);
  }

  @PatchMapping("/{accountId}")
  public ResponseEntity<Account> updateAccountById(
      @PathVariable Long accountId,
      @RequestBody AccountRequest accountRequest,
      @RequestHeader("Authorization") String authHeader) {

    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);
    Account updatedAccount = accountService.updateAccountById(accountId, accountRequest, token);
    return ResponseEntity.ok(updatedAccount);
  }

  @DeleteMapping("/{accountId}")
  public ResponseEntity<String> deleteAccountById(
      @PathVariable Long accountId,
      @RequestHeader("Authorization") String authHeader) {

    String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);
    accountService.deleteAccountById(accountId, token);
    return ResponseEntity.ok("Account deleted successfully");
  }
}
