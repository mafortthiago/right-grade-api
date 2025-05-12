package com.mafort.rightgrade.domain.authentication;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class AccountConfirmationToken {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;
        private String token;
        private String email;
        private Instant expiration;

        public AccountConfirmationToken(String token, String email, Instant expiration) {
                this.token = token;
                this.email = email;
                this.expiration = expiration;
        }
}
