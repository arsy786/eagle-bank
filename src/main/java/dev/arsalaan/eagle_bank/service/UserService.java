package dev.arsalaan.eagle_bank.service;

import dev.arsalaan.eagle_bank.dto.LoginRequest;
import dev.arsalaan.eagle_bank.dto.JwtResponse;
import dev.arsalaan.eagle_bank.dto.RegisterRequest;
import dev.arsalaan.eagle_bank.dto.UpdateUserRequest;
import dev.arsalaan.eagle_bank.model.Account;
import dev.arsalaan.eagle_bank.model.User;
import dev.arsalaan.eagle_bank.repository.AccountRepository;
import dev.arsalaan.eagle_bank.repository.UserRepository;
import dev.arsalaan.eagle_bank.security.JwtTokenUtil;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AccountRepository accountRepository;

  public UserService(
      AuthenticationManager authenticationManager,
      JwtTokenUtil jwtTokenUtil,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      AccountRepository accountRepository) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.accountRepository = accountRepository;
  }

  public User getUserById(Long userId, String token) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!user.getEmail().equals(email)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
    }

    return user;
  }

  public User updateUserById(Long userId, UpdateUserRequest updateUserRequest, String token) {
    User existingUser = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!existingUser.getEmail().equals(email)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
    }

    existingUser.setEmail(updateUserRequest.getEmail());
    existingUser.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));

    return userRepository.save(existingUser);
  }

  public JwtResponse login(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String accessToken = jwtTokenUtil.generateToken(authentication);
    return new JwtResponse(loginRequest.getEmail(), accessToken);
  }

  public void deleteUserById(Long userId, String token) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    List<Account> accounts = accountRepository.findByUserId(userId);

    if (!accounts.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User has a bank account");
    }

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!user.getEmail().equals(email)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
    }

    userRepository.delete(user);
  }

  public void register(RegisterRequest registerRequest) {

    if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already taken");
    }

    User newUser = User.builder()
        .email(registerRequest.getEmail())
        .password(passwordEncoder.encode(registerRequest.getPassword()))
        .build();

    userRepository.save(newUser);
  }
}
