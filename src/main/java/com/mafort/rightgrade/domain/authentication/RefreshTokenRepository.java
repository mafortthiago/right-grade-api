package com.mafort.rightgrade.domain.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    RefreshToken findByTeacherId(UUID id);
}
