package dev.arsalaan.eagle_bank.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String email;
    private String password;
}