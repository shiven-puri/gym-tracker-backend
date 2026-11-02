
package com.example.gym_tracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.util.matcher.RequestMatcher;
import jakarta.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers ->
                        headers.frameOptions(frame -> frame.sameOrigin())
                )

                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // Registration and login
                        .requestMatchers("/api/auth/**").permitAll()

                        // Public exercise catalogue
                        .requestMatchers(HttpMethod.GET, "/api/exercises").permitAll()

                        .requestMatchers("/", "/index.html").permitAll()

                        // Public crowd statistics and analytics
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/crowd/stats/**",
                                "/api/analytics/**"
                        ).permitAll()

                        // Submitting crowd reports requires login
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/crowd/report"
                        ).authenticated()

                        // Only administrators may modify or delete crowd reports
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/crowd/report/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/crowd/report/**"
                        ).hasRole("ADMIN")

                        // Swagger and API documentation
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api-docs/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopesConverter =
                new JwtGrantedAuthoritiesConverter();

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter =
                jwt -> {
                    Collection<GrantedAuthority> authorities =
                            new ArrayList<>();

                    Collection<GrantedAuthority> scopeAuthorities =
                            scopesConverter.convert(jwt);

                    if (scopeAuthorities != null) {
                        authorities.addAll(scopeAuthorities);
                    }

                    String role = jwt.getClaimAsString("role");

                    if (role != null && !role.isBlank()) {
                        authorities.add(
                                new SimpleGrantedAuthority("ROLE_" + role)
                        );
                    }

                    return authorities;
                };

        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}
