
package com.example.gym_tracker.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name = "body_metrics")
public class BodyMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonIgnore
    private User user;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate logDate = LocalDate.now();

    @DecimalMin(value = "0.1", message = "Weight must be greater than 0")
    private Double weight;

    @DecimalMin(value = "0.1", message = "Muscle mass must be greater than 0")
    private Double muscleMass;

    @DecimalMin(value = "0.0", message = "Fat percentage cannot be negative")
    private Double fatPercentage;
}
