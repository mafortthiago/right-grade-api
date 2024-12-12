package com.mafort.rightgrade.infra.security;

import java.util.UUID;

public record JWTDTO(String accessToken, String refreshToken, UUID id) {
}
