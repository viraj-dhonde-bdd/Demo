package com.example.demo.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.Entity.User;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // No Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token
        String token = authHeader.substring(7);
        
        String jwt = null;

        // =========================================
        // Read JWT from Cookie
        // =========================================

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                if ("JWT".equals(cookie.getName())) {

                    jwt = cookie.getValue();

                    break;
                }
            }
        }

        try {

            // Extract email from JWT
            String email = jwtService.extractEmail(token);

            if (email != null
                    && SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                // Find user in database
                User user = userRepository
                        .findByEmail(email)
                        .orElse(null);

                // Validate token
                if (user != null && jwtService.isTokenValid(token, user)) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of(
                                        new SimpleGrantedAuthority(
                                            user.getRole()
                                        )
                                    )
                            );

                    // Set authenticated user
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (Exception e) {

            // Invalid/expired JWT.
            // Leave the request unauthenticated.
        }

        // Continue request
        filterChain.doFilter(request, response);
    }
}
