package com.mafort.rightgrade.domain.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountConfirmationTokenRepository extends JpaRepository<AccountConfirmationToken, String> {
    Optional<AccountConfirmationToken> findByToken(String token);
}

