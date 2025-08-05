package dev.arsalaan.eagle_bank.controller;

import dev.arsalaan.eagle_bank.dto.LoginRequest;
import dev.arsalaan.eagle_bank.dto.JwtResponse;
import dev.arsalaan.eagle_bank.dto.RegisterRequest;
import dev.arsalaan.eagle_bank.dto.UserRequest;
import dev.arsalaan.eagle_bank.dto.UserResponse;
import dev.arsalaan.eagle_bank.security.JwtTokenUtil;
import dev.arsalaan.eagle_bank.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserByToken(@RequestHeader("Authorization") String authHeader) {
        log.info("GET /v1/users/me called");

        String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);
        UserResponse user = userService.getUserByToken(token);

        log.info("Current user retrieved successfully: {}", user.getEmail());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId,
            @RequestHeader("Authorization") String authHeader) {

        log.info("GET /v1/users/{} called", userId);
        String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);

        UserResponse user = userService.getUserById(userId, token);

        log.info("User with id {} retrieved successfully", userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("POST /v1/users/login called for email: {}", loginRequest.getEmail());
        JwtResponse jwtResponse = userService.login(loginRequest);
        log.info("User {} logged in successfully", loginRequest.getEmail());
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("POST /v1/users called to register user: {} {} ({})",
                registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getEmail());

        userService.register(registerRequest);

        log.info("User {} {} ({}) registered successfully",
                registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable Long userId,
            @Valid @RequestBody UserRequest updateUserRequest,
            @RequestHeader("Authorization") String authHeader) {

        log.info("PATCH /v1/users/{} called", userId);

        String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);

        UserResponse updatedUser = userService.updateUserById(userId, updateUserRequest, token);

        log.info("User {} {} updated successfully", updatedUser.getFirstName(), updatedUser.getLastName());
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId,
            @RequestHeader("Authorization") String authHeader) {

        log.info("DELETE /v1/users/{} called", userId);

        String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);

        userService.deleteUserById(userId, token);

        log.info("User with id {} deleted successfully", userId);
        return ResponseEntity.noContent().build();
    }

}
