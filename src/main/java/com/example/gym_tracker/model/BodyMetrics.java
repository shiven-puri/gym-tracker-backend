package com.example.gym_tracker.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Data
@Table(name="body_metrics")

public class BodyMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate logDate = LocalDate.now();
    @DecimalMin(value = "0.1", message = "Weight must be greater than 0")
    private Double weight;

    @DecimalMin(value = "0.1", message = "Muscle mass must be greater than 0")
    private Double muscleMass;

    @DecimalMin(value = "0.0", message = "Fat percentage cannot be negative")
    private Double fatPercentage;
}
