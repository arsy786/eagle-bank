package dev.arsalaan.eagle_bank.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dev.arsalaan.eagle_bank.dto.AccountRequest;
import dev.arsalaan.eagle_bank.model.Account;
import dev.arsalaan.eagle_bank.model.User;
import dev.arsalaan.eagle_bank.repository.AccountRepository;
import dev.arsalaan.eagle_bank.repository.UserRepository;
import dev.arsalaan.eagle_bank.security.JwtTokenUtil;

@Service
public class AccountService {

  private final AccountRepository accountRepository;
  private final UserRepository userRepository;
  private final JwtTokenUtil jwtTokenUtil;

  public AccountService(AccountRepository accountRepository, UserRepository userRepository,
      JwtTokenUtil jwtTokenUtil) {
    this.accountRepository = accountRepository;
    this.userRepository = userRepository;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  public Account createAccount(AccountRequest accountRequest, String token) {
    String email = jwtTokenUtil.getUsernameFromToken(token);

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    Account account = Account.builder()
        .accountName(accountRequest.getAccountName())
        .accountType(accountRequest.getAccountType())
        .accountNumber(generateAccountNumber())
        .balance(BigDecimal.ZERO)
        .createdAt(LocalDateTime.now())
        .user(user)
        .build();

    return accountRepository.save(account);
  }

  public Account getAccountById(Long accountId, String token) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!account.getUser().getEmail().equals(email)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this account");
    }

    return account;
  }

  public List<Account> getAllAccounts(String token) {
    String email = jwtTokenUtil.getUsernameFromToken(token);

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    return accountRepository.findByUserId(user.getId());
  }

  public Account updateAccountById(Long accountId, AccountRequest accountRequest, String token) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!account.getUser().getEmail().equals(email)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this account");
    }

    // Only update fields that are provided in the request (PATCH semantics)
    if (accountRequest.getAccountName() != null) {
      account.setAccountName(accountRequest.getAccountName());
    }
    if (accountRequest.getAccountType() != null) {
      account.setAccountType(accountRequest.getAccountType());
    }

    return accountRepository.save(account);
  }

  public void deleteAccountById(Long accountId, String token) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!account.getUser().getEmail().equals(email)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this account");
    }

    accountRepository.delete(account);
  }

  private String generateAccountNumber() {
    return UUID.randomUUID().toString().replace("-", "").substring(0, 12); // 12-digit pseudo account number
  }

}
