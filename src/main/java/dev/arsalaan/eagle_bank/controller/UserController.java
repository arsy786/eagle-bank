package dev.arsalaan.eagle_bank.controller;

import dev.arsalaan.eagle_bank.dto.JwtRequest;
import dev.arsalaan.eagle_bank.dto.JwtResponse;
import dev.arsalaan.eagle_bank.dto.RegisterRequest;
import dev.arsalaan.eagle_bank.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody JwtRequest jwtRequest) {
        JwtResponse jwtResponse = userService.login(jwtRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.status(201).body("User registered successfully");
    }
}
