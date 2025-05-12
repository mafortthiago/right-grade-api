package com.mafort.rightgrade.infra.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class SecurityFilter extends OncePerRequestFilter {
    private static final String ACCESS_TOKEN = "accessToken";

    @Autowired private MessageSource messageSource;
    @Autowired
    private TokenService tokenService;

    private UserDetailsService userDetailsService;

    @Autowired
    public void setUserDetailsService(@Lazy UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var requestURI = request.getRequestURI();
        var isAuthRequest =
                requestURI.contains("/register")
                || requestURI.contains("/login")
                || requestURI.contains("/refresh-token")
                || requestURI.contains("/auth/verify-code")
                || requestURI.contains("/auth/send-code")
                || requestURI.contains("/auth/reset-password")
                || requestURI.contains("/auth/confirm-account");

        if(!isAuthRequest){
            try {
                String accessToken = extractTokenFromCookie(request, ACCESS_TOKEN);
                String subject = tokenService.getSubject(accessToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JWTVerificationException e) {
                SecurityContextHolder.clearContext();
                invalidateCookie(ACCESS_TOKEN, response);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(
                        "{\"error\": \""+ messageSource.getMessage(
                                "error.refreshToken.invalid",
                                null,
                                LocaleContextHolder.getLocale())+"\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void invalidateCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String extractTokenFromCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
}}