package com.mafort.rightgrade.domain.verificationCode;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Table(name = "code")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String email;
    private String code;
    @Getter
    private Instant expiration;

    public VerificationCode(String email, String code, Instant expiration) {
        this.email = email;
        this.code = code;
        this.expiration = expiration;
    }
}
