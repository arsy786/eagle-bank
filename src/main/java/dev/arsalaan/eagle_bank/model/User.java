package dev.arsalaan.eagle_bank.model;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Data
@Entity
@Table(name = "users") // user is a reserved keyword therefore must rename to users
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

}
