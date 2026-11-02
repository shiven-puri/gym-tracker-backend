
package com.example.gym_tracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Table(name = "workout_logs")
@Data
public class WorkoutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private User user;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate logDate = LocalDate.now();

    @DecimalMin(value = "0.1", message = "Weight lifted must be greater than 0")
    private Double weightLifted;

    @Min(value = 1, message = "Reps must be at least 1")
    private Integer reps;

    @Min(value = 1, message = "Sets must be at least 1")
    private Integer sets;
}
