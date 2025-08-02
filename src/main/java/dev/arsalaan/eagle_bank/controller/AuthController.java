package dev.arsalaan.eagle_bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.arsalaan.eagle_bank.dto.JwtRequest;
import dev.arsalaan.eagle_bank.dto.JwtResponse;
import dev.arsalaan.eagle_bank.model.User;
import dev.arsalaan.eagle_bank.repository.UserRepository;
import dev.arsalaan.eagle_bank.security.JwtTokenUtil;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
        AuthenticationManager authenticationManager,
        JwtTokenUtil jwtTokenUtil,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody JwtRequest jwtRequest){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token form jwtTokenUtil
        String accessToken = jwtTokenUtil.generateToken(authentication);
        JwtResponse jwtResponse = new JwtResponse(jwtRequest.getEmail(), accessToken);

        return ResponseEntity.ok().body(jwtResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody JwtRequest registerUser){

        // add check for email exists in DB
        if(userRepository.findByEmail(registerUser.getEmail()).isPresent()){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        // create and save new user object
        User saveUser = new User();
        saveUser.setEmail(registerUser.getEmail());
        saveUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));

        userRepository.save(saveUser);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }


}
