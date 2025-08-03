package dev.arsalaan.eagle_bank.controller;

import dev.arsalaan.eagle_bank.dto.LoginRequest;
import dev.arsalaan.eagle_bank.dto.JwtResponse;
import dev.arsalaan.eagle_bank.dto.RegisterRequest;
import dev.arsalaan.eagle_bank.dto.UpdateUserRequest;
import dev.arsalaan.eagle_bank.model.User;
import dev.arsalaan.eagle_bank.security.JwtTokenUtil;
import dev.arsalaan.eagle_bank.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

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

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId,
            @RequestHeader("Authorization") String authHeader) {

        log.info("GET /v1/users/{} called", userId);
        String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);

        User user = userService.getUserById(userId, token);

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
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("POST /v1/users called to register user with email: {}", registerRequest.getEmail());
        userService.register(registerRequest);
        log.info("User {} registered successfully", registerRequest.getEmail());
        return ResponseEntity.status(201).body("User registered successfully");
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUserById(@PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequest updateUserRequest,
            @RequestHeader("Authorization") String authHeader) {

        log.info("PATCH /v1/users/{} called", userId);

        String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);

        User updatedUser = userService.updateUserById(userId, updateUserRequest, token);

        log.info("User with id {} updated successfully", userId);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId,
            @RequestHeader("Authorization") String authHeader) {

        log.info("DELETE /v1/users/{} called", userId);

        String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);

        userService.deleteUserById(userId, token);

        log.info("User with id {} deleted successfully", userId);
        return ResponseEntity.ok("User deleted successfully");
    }

}
