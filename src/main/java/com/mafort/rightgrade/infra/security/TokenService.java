package com.mafort.rightgrade.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.mafort.rightgrade.domain.teacher.Teacher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.service}")
    private String secret;

    public String generateToken(Teacher teacher) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("RightGrade")
                    .withSubject(teacher.getEmail())
                    .withExpiresAt(dateExpiration(5))  // 5 horas de validade
                    .sign(algorithm);
        } catch (JWTCreationException jwtCreationException) {
            throw new RuntimeException("Error, unable to generate token");
        }
    }

    public String generateRefreshToken(Teacher teacher) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("RightGrade")
                    .withSubject(teacher.getEmail())
                    .withExpiresAt(dateExpiration(30 * 24))  // 30 dias de validade
                    .sign(algorithm);
        } catch (JWTCreationException jwtCreationException) {
            throw new RuntimeException("Error, unable to generate token");
        }
    }

    public String getSubject(String jwt) {
        var algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("RightGrade")
                .build()
                .verify(jwt)
                .getSubject();
    }

    private Instant dateExpiration(int hours) {
        return LocalDateTime.now().plusHours(hours).toInstant(ZoneOffset.of("-03:00"));
    }
}
