package dev.arsalaan.eagle_bank.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import dev.arsalaan.eagle_bank.dto.AccountRequest;
import dev.arsalaan.eagle_bank.dto.AccountResponse;
import dev.arsalaan.eagle_bank.exception.ApiRequestException;
import dev.arsalaan.eagle_bank.mapper.AccountMapper;
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
  private final AccountMapper accountMapper;

  public AccountService(AccountRepository accountRepository, UserRepository userRepository,
      JwtTokenUtil jwtTokenUtil, AccountMapper accountMapper) {
    this.accountRepository = accountRepository;
    this.userRepository = userRepository;
    this.jwtTokenUtil = jwtTokenUtil;
    this.accountMapper = accountMapper;
  }

  public List<AccountResponse> getAllAccounts(String token) {
    String email = jwtTokenUtil.getUsernameFromToken(token);

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ApiRequestException(HttpStatus.NOT_FOUND, "User not found"));

    List<Account> accounts = accountRepository.findByUserId(user.getId());

    return accountMapper.toAccountResponseList(accounts);
  }

  public AccountResponse getAccountById(Long accountId, String token) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ApiRequestException(HttpStatus.NOT_FOUND, "Account not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!account.getUser().getEmail().equals(email)) {
      throw new ApiRequestException(HttpStatus.FORBIDDEN, "You are not authorized to access this account");
    }

    return accountMapper.toAccountResponse(account);
  }

  public AccountResponse createAccount(AccountRequest accountRequest, String token) {
    String email = jwtTokenUtil.getUsernameFromToken(token);

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ApiRequestException(HttpStatus.NOT_FOUND, "User not found"));

    Account account = accountMapper.toAccount(accountRequest);

    account.setUser(user);
    account.setAccountNumber(generateAccountNumber());
    account.setBalance(BigDecimal.ZERO);
    account.setCreatedAt(LocalDateTime.now());

    Account savedAccount = accountRepository.save(account);

    return accountMapper.toAccountResponse(savedAccount);
  }

  public AccountResponse updateAccountById(Long accountId, AccountRequest accountRequest, String token) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ApiRequestException(HttpStatus.NOT_FOUND, "Account not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!account.getUser().getEmail().equals(email)) {
      throw new ApiRequestException(HttpStatus.FORBIDDEN, "You are not authorized to update this account");
    }

    // Only update fields that are provided in the request (PATCH semantics)
    if (accountRequest.getAccountName() != null) {
      account.setAccountName(accountRequest.getAccountName());
    }
    if (accountRequest.getAccountType() != null) {
      account.setAccountType(accountRequest.getAccountType());
    }

    Account updatedAccount = accountRepository.save(account);

    return accountMapper.toAccountResponse(updatedAccount);
  }

  public void deleteAccountById(Long accountId, String token) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ApiRequestException(HttpStatus.NOT_FOUND, "Account not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!account.getUser().getEmail().equals(email)) {
      throw new ApiRequestException(HttpStatus.FORBIDDEN, "You are not authorized to delete this account");
    }

    accountRepository.delete(account);
  }

  private String generateAccountNumber() {
    return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
  }

}
