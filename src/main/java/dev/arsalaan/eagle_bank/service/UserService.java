package dev.arsalaan.eagle_bank.service;

import dev.arsalaan.eagle_bank.dto.JwtRequest;
import dev.arsalaan.eagle_bank.dto.JwtResponse;
import dev.arsalaan.eagle_bank.dto.RegisterRequest;
import dev.arsalaan.eagle_bank.model.User;
import dev.arsalaan.eagle_bank.repository.UserRepository;
import dev.arsalaan.eagle_bank.security.JwtTokenUtil;

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

    public UserService(
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

    public JwtResponse login(JwtRequest jwtRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenUtil.generateToken(authentication);
        return new JwtResponse(jwtRequest.getEmail(), accessToken);
    }

    public void register(RegisterRequest registerRequest) {

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already taken");
        }

        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(newUser);
    }
}
