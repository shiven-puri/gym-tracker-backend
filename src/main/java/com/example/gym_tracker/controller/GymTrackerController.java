package com.example.gym_tracker.controller;

import com.example.gym_tracker.dto.SlotSuggestionDTO;
import com.example.gym_tracker.model.BodyMetrics;
import com.example.gym_tracker.model.CrowdLog;
import com.example.gym_tracker.model.Exercise;
import com.example.gym_tracker.model.WorkoutLog;
import com.example.gym_tracker.service.BodyMetricsService;
import com.example.gym_tracker.service.CrowdLogService;
import com.example.gym_tracker.service.ExerciseService;
import com.example.gym_tracker.service.WorkoutLogService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@Tag(
        name = "Gym Tracker API",
        description = "APIs for workout tracking, body metrics, exercises, and gym crowd analytics"
)

@RestController
@RequestMapping("/api")
public class GymTrackerController {

    private final BodyMetricsService bodyMetricsService;
    private final ExerciseService exerciseService;
    private final WorkoutLogService workoutLogService;
    private final CrowdLogService crowdLogService;

    public GymTrackerController(
            BodyMetricsService bodyMetricsService,
            ExerciseService exerciseService,
            WorkoutLogService workoutLogService,
            CrowdLogService crowdLogService
    ) {
        this.bodyMetricsService = bodyMetricsService;
        this.exerciseService = exerciseService;
        this.workoutLogService = workoutLogService;
        this.crowdLogService = crowdLogService;
    }


    // -------------------------------
    // Body Metrics
    // -------------------------------


    @Operation(
            summary = "Create body metrics",
            description = "Records the user's weight, muscle mass, and body fat percentage."
    )
    @PostMapping("/body-metrics")
    public BodyMetrics logBodyMetrics(
            @Valid@RequestBody BodyMetrics metrics
    ) {
        return bodyMetricsService.logBodyMetrics(metrics);
    }

    @Operation(
            summary = "Get body metrics history",
            description = "Returns the user's previously recorded body metrics."
    )
    @GetMapping("/body-metrics/history")
    public List<BodyMetrics> getBodyMetricsHistory() {
        return bodyMetricsService.getBodyMetricsHistory();
    }

    @Operation(
            summary = "Delete body metrics",
            description = "Deletes a body metrics record by its ID."
    )
    @DeleteMapping("/body-metrics/{id}")
    public String deleteBodyMetrics(
            @PathVariable Long id
    ) {
        bodyMetricsService.deleteBodyMetrics(id);
        return "Body metric entry with ID " + id
                + " was successfully deleted.";
    }

    @PutMapping("/body-metrics/{id}")
    public BodyMetrics updateBodyMetrics(
            @PathVariable Long id,
            @Valid@RequestBody BodyMetrics updatedMetrics
    ) {
        return bodyMetricsService.updateBodyMetrics(
                id,
                updatedMetrics
        );
    }


    // -------------------------------
    // Exercises
    // -------------------------------

    @Operation(
            summary = "Create exercise",
            description = "Creates a new exercise with its name and muscle group."
    )
    @PostMapping("/exercises")
    public Exercise createExercise(
            @Valid@RequestBody Exercise exercise
    ) {
        return exerciseService.createExercise(exercise);
    }

    @Operation(
            summary = "Get all exercises",
            description = "Returns all exercises available in the system."
    )
    @GetMapping("/exercises")
    public List<Exercise> getAllExercises() {
        return exerciseService.getAllExercises();
    }

    @Operation(
            summary = "Delete exercise",
            description = "Deletes an exercise by its ID."
    )
    @DeleteMapping("/exercises/{id}")
    public String deleteExercise(
            @PathVariable Long id
    ) {
        exerciseService.deleteExercise(id);
        return "Exercise with ID " + id
                + " and all its related logs deleted successfully.";
    }


    // -------------------------------
    // Workouts
    // -------------------------------

    @Operation(
            summary = "Log a workout",
            description = "Records a workout for an exercise, including weight lifted, repetitions, and sets."
    )
    @PostMapping("/workouts/log")
    public WorkoutLog logWorkout(
            @Valid@RequestBody WorkoutLog log
    ) {
        return workoutLogService.logWorkout(log);
    }

    @Operation(
            summary = "Get workout progress",
            description = "Returns the workout progress history for a specific exercise."
    )
    @GetMapping("/workouts/progress/{exerciseId}")
    public List<WorkoutLog> getExerciseProgress(
            @PathVariable Long exerciseId
    ) {
        return workoutLogService.getExerciseProgress(exerciseId);
    }

    @Operation(
            summary = "Delete workout log",
            description = "Deletes a workout log by its ID."
    )
    @DeleteMapping("/workouts/log/{id}")
    public String deleteWorkoutLog(
            @PathVariable Long id
    ) {
        workoutLogService.deleteWorkoutLog(id);
        return "Workout log with ID " + id
                + " was successfully deleted.";
    }

    @Operation(
            summary = "Update workout log",
            description = "Updates an existing workout log by its ID."
    )
    @PutMapping("/workouts/log/{id}")
    public WorkoutLog updateWorkoutLog(
            @PathVariable Long id,
            @Valid@RequestBody WorkoutLog updatedLog
    ) {
        return workoutLogService.updateWorkoutLog(
                id,
                updatedLog
        );
    }


    // -------------------------------
    // Crowd
    // -------------------------------

    @Operation(
            summary = "Report gym crowd level",
            description = "Records the crowd level for a specific day and time slot."
    )
    @PostMapping("/crowd/report")
    public CrowdLog reportCrowd(
            @Valid@RequestBody CrowdLog log
    ) {
        return crowdLogService.reportCrowd(log);
    }

    @Operation(
            summary = "Get crowd statistics",
            description = "Returns crowd statistics for a specific day of the week."
    )
    @GetMapping("/crowd/stats/{day}")
    public List<CrowdLog> getCrowdStatsByDay(
            @PathVariable String day
    ) {
        return crowdLogService.getCrowdStatsByDay(day);
    }

    @Operation(
            summary = "Delete crowd report",
            description = "Deletes a crowd report by its ID."
    )
    @DeleteMapping("/crowd/report/{id}")
    public String deleteCrowdReport(
            @PathVariable Long id
    ) {
        crowdLogService.deleteCrowdReport(id);
        return "Crowd report with ID " + id
                + " was successfully deleted.";
    }

    @Operation(
            summary = "Update crowd report",
            description = "Updates an existing crowd report by its ID."
    )
    @PutMapping("/crowd/report/{id}")
    public CrowdLog updateCrowdReport(
            @PathVariable Long id,
            @Valid@RequestBody CrowdLog updatedReport
    ) {
        return crowdLogService.updateCrowdReport(
                id,
                updatedReport
        );
    }


    // -------------------------------
    // Analytics
    // -------------------------------

    @Operation(
            summary = "Get optimal gym time slots",
            description = "Returns gym time slots ordered by average crowd level for a specific day."
    )
    @GetMapping("/analytics/optimal-slots/{day}")
    public List<SlotSuggestionDTO> getOptimalTimeSlots(
            @PathVariable String day
    ) {
        return crowdLogService.getOptimalTimeSlots(day);
    }
}