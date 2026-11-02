
package com.example.gym_tracker.dto;

public record AuthResponse(
        String token,
        String tokenType,
        Long userId,
        String email
) {}
