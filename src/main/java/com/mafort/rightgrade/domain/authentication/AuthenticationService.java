package com.mafort.rightgrade.domain.authentication;

import com.mafort.rightgrade.domain.teacher.Teacher;
import com.mafort.rightgrade.domain.teacher.TeacherRepository;
import com.mafort.rightgrade.infra.security.JWTDTO;
import com.mafort.rightgrade.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TeacherRepository teacherRepository;

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
}