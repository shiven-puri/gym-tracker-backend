
package com.example.gym_tracker.service;

import com.example.gym_tracker.dto.AuthResponse;
import com.example.gym_tracker.model.User;
import com.example.gym_tracker.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Locale;

@Service
public class AuthService {

    private static final long TOKEN_LIFETIME_SECONDS = 3600;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Transactional
    public AuthResponse register(String email, String rawPassword) {
        String normalizedEmail = normalizeEmail(email);

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An account with this email already exists."
            );
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(normalizedEmail, hashedPassword);
        User savedUser = userRepository.save(user);

        return createAuthResponse(savedUser);
    }

    public AuthResponse login(String email, String rawPassword) {
        String normalizedEmail = normalizeEmail(email);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password."
                ));

        if (!passwordEncoder.matches(
                rawPassword, user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password."
            );
        }

        return createAuthResponse(user);
    }

    private AuthResponse createAuthResponse(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(TOKEN_LIFETIME_SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("gym-tracker")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getEmail())
                .claim("uid", user.getId())
                .claim("role", user.getRole().name())
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getEmail()
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
