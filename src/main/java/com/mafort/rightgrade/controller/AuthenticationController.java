package com.mafort.rightgrade.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mafort.rightgrade.domain.authentication.*;
import com.mafort.rightgrade.domain.teacher.*;
import com.mafort.rightgrade.infra.security.JWTDTO;
import com.mafort.rightgrade.infra.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.UUID;


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
    @Autowired MessageSource messageSource;

    @PostMapping("/login")
    public ResponseEntity<JWTDTO> login(@Valid @RequestBody LoginRequestDTO login, HttpServletResponse response) {
        JWTDTO jwtDto = authenticationService.authenticate(login.email(), login.password());
        this.authenticationService.addAuthCookies(response, jwtDto.accessToken(), jwtDto.refreshToken());
        return ResponseEntity.ok(new JWTDTO(null, null, jwtDto.id()));
    }

    @PostMapping("/register")
    public ResponseEntity<JWTDTO> register(@Valid @RequestBody RegisterDTO registerDTO, HttpServletResponse response) {
        JWTDTO jwtDto = authenticationService.register(registerDTO);
        authenticationService.addAuthCookies(response, jwtDto.accessToken(), jwtDto.refreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new JWTDTO(null, null, jwtDto.id()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String refreshTokenValue = authenticationService.extractRefreshTokenFromCookies(request.getCookies());

            if (refreshTokenValue == null) {
                String errorMessage = messageSource.getMessage("error.refreshToken.notFound", null, LocaleContextHolder.getLocale());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", errorMessage));
            }
            JWTDTO jwtDto = authenticationService.refreshToken(refreshTokenValue);
            authenticationService.addAuthCookies(response, jwtDto.accessToken(), jwtDto.refreshToken());

            return ResponseEntity.ok(new JWTDTO(null, null, jwtDto.id()));
        } catch (JWTVerificationException e) {
            String errorMessage = messageSource.getMessage("error.refreshToken.invalid", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", errorMessage));
        }
    }

    @PostMapping("/auth/validate-password")
    public ResponseEntity<?> validatePassword(@RequestBody PasswordValidationRequest passwordValidationRequest,
                                              @RequestHeader(value = "Accept-Language", required = false) String language) {
        this.teacherService.validatePassword(passwordValidationRequest);
        this.teacherService.sendEmail(passwordValidationRequest.email(), language);
        return ResponseEntity.ok().build();
    }

    @PostMapping("auth/send-code")
    public ResponseEntity<Void>sendCode(@RequestBody @Valid SendCodeRequest request,
                                        @RequestHeader(value = "Accept-Language", required = false) String language){
        this.teacherService.sendEmail(request.email(), language);
        return ResponseEntity.ok().build();
    }

    @PostMapping("auth/verify-code")
    public ResponseEntity<?>sendCode(@RequestBody @Valid VerifyCodeRequest request){
        String code = this.teacherService.validateCode( request.email(), request.code());
        return ResponseEntity.ok().body(Map.of("code", code));
    }

    @PostMapping("auth/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        teacherService.changePassword(
                request.email(),
                request.newPassword(),
                request.code()
        );

        String successMessage = messageSource.getMessage("success.password.updated", null, LocaleContextHolder.getLocale());
        return ResponseEntity.ok().body(Map.of("message", successMessage));
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ChangePasswordRequest request) {
        teacherService.changePassword(
                request.email(),
                request.newPassword(),
                request.code()
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("me")
    public ResponseEntity<Map<String,UUID>> checkAuth(@AuthenticationPrincipal Teacher teacher){
        UUID id = this.authenticationService.checkAuth(teacher);
        return ResponseEntity.ok(Map.of("id", id));
    }
}

