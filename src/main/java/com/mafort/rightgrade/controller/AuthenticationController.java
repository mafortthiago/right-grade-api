package com.mafort.rightgrade.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mafort.rightgrade.domain.authentication.*;
import com.mafort.rightgrade.domain.teacher.*;
import com.mafort.rightgrade.infra.security.JWTDTO;
import com.mafort.rightgrade.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;


@RestController
public class AuthenticationController {
    @Autowired TeacherService teacherService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<JWTDTO> login(@Valid @RequestBody LoginRequestDTO login, HttpServletResponse response) {
        JWTDTO jwtDto = authenticationService.authenticate(login.email(), login.password());
        return ResponseEntity.ok(jwtDto);
    }

    @PostMapping("/register")
    public ResponseEntity<JWTDTO> register(@Valid @RequestBody RegisterDTO registerDTO, HttpServletResponse response) {
        var teacher = new Teacher(registerDTO);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherRepository.save(teacher);
        var tokenJWT = tokenService.generateToken(teacher);
        var refreshToken = tokenService.generateRefreshToken(teacher);
        refreshTokenRepository.save(new RefreshToken(refreshToken,teacher));
        return ResponseEntity.status(HttpStatus.CREATED).body(new JWTDTO(tokenJWT, refreshToken, teacher.getId()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JWTDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshToken) {
        try {
            String email = tokenService.getSubject(refreshToken.jwt());
            var teacher = (Teacher)teacherRepository.findByEmail(email);
            var actualToken = refreshTokenRepository.findByTeacherId(teacher.getId());
            if (actualToken == null) {
                throw new JWTVerificationException("Refresh token not found");
            }
            if (!refreshToken.jwt().equals(actualToken.getToken())) {
                throw new JWTVerificationException("Invalid jwt");
            }
            refreshTokenRepository.deleteById(actualToken.getId());
            var newToken = tokenService.generateToken(teacher);
            var newRefreshToken = tokenService.generateRefreshToken( teacher);
            refreshTokenRepository.save(new RefreshToken(newRefreshToken, teacher));
            return ResponseEntity.ok(new JWTDTO(newToken, newRefreshToken, teacher.getId()));
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/auth/validate-password")
    public ResponseEntity<?> validatePassword(@RequestBody PasswordValidationRequest passwordValidationRequest,
                                              @RequestHeader(value = "Accept-Language", required = false) String language) {
        this.teacherService.validatePassword(passwordValidationRequest);
        this.teacherService.sendEmail(passwordValidationRequest.email(), language);
        return ResponseEntity.ok().build();
    }

    @PostMapping("auth/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                            @RequestHeader(value = "Accept-Language", required = false) String language) {
        teacherService.changePassword(
                request.email(),
                request.newPassword(),
                request.code(),
                language
        );

        return ResponseEntity.ok().body("Password updated successfully");
    }
}

