package dev.arsalaan.eagle_bank.service;

import dev.arsalaan.eagle_bank.dto.LoginRequest;
import dev.arsalaan.eagle_bank.dto.JwtResponse;
import dev.arsalaan.eagle_bank.dto.RegisterRequest;
import dev.arsalaan.eagle_bank.dto.UserRequest;
import dev.arsalaan.eagle_bank.dto.UserResponse;
import dev.arsalaan.eagle_bank.exception.ApiRequestException;
import dev.arsalaan.eagle_bank.mapper.UserMapper;
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

@Service
public class UserService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AccountRepository accountRepository;
  private final UserMapper userMapper;

  public UserService(
      AuthenticationManager authenticationManager,
      JwtTokenUtil jwtTokenUtil,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      AccountRepository accountRepository,
      UserMapper userMapper) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.accountRepository = accountRepository;
    this.userMapper = userMapper;
  }

  public UserResponse getUserById(Long userId, String token) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ApiRequestException(HttpStatus.NOT_FOUND, "User not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!user.getEmail().equals(email)) {
      throw new ApiRequestException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
    }

    return userMapper.toUserResponse(user);
  }

  public UserResponse updateUserById(Long userId, UserRequest updateUserRequest, String token) {
    User existingUser = userRepository.findById(userId)
        .orElseThrow(() -> new ApiRequestException(HttpStatus.NOT_FOUND, "User not found"));

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!existingUser.getEmail().equals(email)) {
      throw new ApiRequestException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
    }

    // Only update fields that are provided (PATCH semantics)
    if (updateUserRequest.getFirstName() != null) {
      existingUser.setFirstName(updateUserRequest.getFirstName());
    }
    if (updateUserRequest.getLastName() != null) {
      existingUser.setLastName(updateUserRequest.getLastName());
    }
    if (updateUserRequest.getEmail() != null) {
      existingUser.setEmail(updateUserRequest.getEmail());
    }
    // if (updateUserRequest.getPassword() != null) {
    // existingUser.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
    // }
    if (updateUserRequest.getPhoneNumber() != null) {
      existingUser.setPhoneNumber(updateUserRequest.getPhoneNumber());
    }
    if (updateUserRequest.getDateOfBirth() != null) {
      existingUser.setDateOfBirth(updateUserRequest.getDateOfBirth());
    }

    User updatedUser = userRepository.save(existingUser);

    return userMapper.toUserResponse(updatedUser);
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
        .orElseThrow(() -> new ApiRequestException(HttpStatus.NOT_FOUND, "User not found"));

    List<Account> accounts = accountRepository.findByUserId(userId);

    if (!accounts.isEmpty()) {
      throw new ApiRequestException(HttpStatus.CONFLICT, "User has a bank account");
    }

    String email = jwtTokenUtil.getUsernameFromToken(token);

    if (!user.getEmail().equals(email)) {
      throw new ApiRequestException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
    }

    userRepository.delete(user);
  }

  public void register(RegisterRequest registerRequest) {

    if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
      throw new ApiRequestException(HttpStatus.CONFLICT, "Email is already taken");
    }

    User newUser = User.builder()
        .firstName(registerRequest.getFirstName())
        .lastName(registerRequest.getLastName())
        .email(registerRequest.getEmail())
        .password(passwordEncoder.encode(registerRequest.getPassword()))
        .phoneNumber(registerRequest.getPhoneNumber())
        .dateOfBirth(registerRequest.getDateOfBirth())
        .build();

    userRepository.save(newUser);
  }
}
