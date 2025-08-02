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
import org.springframework.web.bind.annotation.PutMapping;
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

        String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);

        User user = userService.getUserById(userId, token);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = userService.login(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.status(201).body("User registered successfully");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserById(@PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequest updateUserRequest,
            @RequestHeader("Authorization") String authHeader) {

        String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);

        User updatedUser = userService.updateUserById(userId, updateUserRequest, token);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId,
            @RequestHeader("Authorization") String authHeader) {

        String token = jwtTokenUtil.getJwtTokenFromHeader(authHeader);

        userService.deleteUserById(userId, token);
        return ResponseEntity.ok("User deleted successfully");
    }

}
