package com.example.gym_tracker.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "crowd_logs")
@Data
public class CrowdLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private String dayOfWeek;
    private String timeSlot;
    @Min(value = 0, message = "Crowd level cannot be negative")
    @Max(value = 100, message = "Crowd level cannot exceed 100")
    private Integer crowdLevel;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate logDate = LocalDate.now();
}
