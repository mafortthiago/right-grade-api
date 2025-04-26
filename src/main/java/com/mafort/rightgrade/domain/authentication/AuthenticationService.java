package com.mafort.rightgrade.domain.authentication;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mafort.rightgrade.domain.teacher.RegisterDTO;
import com.mafort.rightgrade.domain.teacher.Teacher;
import com.mafort.rightgrade.domain.teacher.TeacherRepository;
import com.mafort.rightgrade.infra.security.JWTDTO;
import com.mafort.rightgrade.infra.security.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService implements UserDetailsService {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TeacherRepository teacherRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private MessageSource messageSource;

    @Autowired
    public AuthenticationService(@Lazy AuthenticationManager authenticationManager,
                                 TokenService tokenService,
                                 RefreshTokenRepository refreshTokenRepository,
                                 TeacherRepository teacherRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = teacherRepository.findByEmail(email);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userDetails;
    }

    public JWTDTO authenticate(String email, String password) {
        Teacher teacher = authenticateTeacher(email, password);
        return generateTokens(teacher);
    }

    private Teacher authenticateTeacher(String email, String password) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        var authentication = authenticationManager.authenticate(authenticationToken);
        return (Teacher) authentication.getPrincipal();
    }

    private JWTDTO generateTokens(Teacher teacher) {
        var tokenJWT = tokenService.generateToken(teacher);

        var actualToken = refreshTokenRepository.findByTeacherId(teacher.getId());
        if(actualToken != null) {
            refreshTokenRepository.deleteById(actualToken.getId());
        }

        var refreshToken = tokenService.generateRefreshToken(teacher);
        refreshTokenRepository.save(new RefreshToken(refreshToken, teacher));

        return new JWTDTO(tokenJWT, refreshToken, teacher.getId());
    }

    private Cookie createCookie(String name, String value, String path, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    public void addAuthCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie accessCookie = createCookie(ACCESS_TOKEN, accessToken, "/", 60 * 60 * 12);
        Cookie refreshCookie = createCookie(REFRESH_TOKEN, refreshToken, "/", 60 * 60 * 24 * 21);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }


    public JWTDTO register(RegisterDTO registerDTO) {
        var teacher = new Teacher(registerDTO);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherRepository.save(teacher);

        return generateTokens(teacher);
    }

    public JWTDTO refreshToken(String refreshTokenValue) {
        String email = tokenService.getSubject(refreshTokenValue);
        var teacher = (Teacher) teacherRepository.findByEmail(email);
        var actualToken = refreshTokenRepository.findByTeacherId(teacher.getId());

        if (actualToken == null || !refreshTokenValue.equals(actualToken.getToken())) {
            String errorMessage = messageSource.getMessage("error.refreshToken.invalid", null, LocaleContextHolder.getLocale());
            throw new JWTVerificationException(errorMessage);
        }

        refreshTokenRepository.deleteById(actualToken.getId());
        return generateTokens(teacher);
    }

    public String extractRefreshTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (REFRESH_TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public UUID checkAuth( Teacher teacher ){
        return  teacher.getId();
    }

}