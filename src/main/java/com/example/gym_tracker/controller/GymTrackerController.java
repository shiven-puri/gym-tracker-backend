package com.example.gym_tracker.controller;

import com.example.gym_tracker.dto.SlotSuggestionDTO;
import com.example.gym_tracker.model.BodyMetrics;
import com.example.gym_tracker.model.Exercise;
import com.example.gym_tracker.model.WorkoutLog;
import com.example.gym_tracker.model.CrowdLog;
import com.example.gym_tracker.repository.BodyMetricsRepository;
import com.example.gym_tracker.repository.ExerciseRepository;
import com.example.gym_tracker.repository.WorkoutLogRepository;
import com.example.gym_tracker.repository.CrowdLogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GymTrackerController {

    @Autowired
    private BodyMetricsRepository bodyMetricsRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutLogRepository workoutLogRepository;

    @Autowired
    private CrowdLogRepository crowdLogRepository;


    @PostMapping("/body-metrics")
    public BodyMetrics logBodyMetrics(@RequestBody BodyMetrics metrics) {
        return bodyMetricsRepository.save(metrics);
    }

    @GetMapping("/body-metrics/history")
    public List<BodyMetrics> getBodyMetricsHistory() {
        return bodyMetricsRepository.findAll();
    }

    @DeleteMapping("/body-metrics/{id}")
    public String deleteBodyMetrics(@PathVariable Long id) {
        if (bodyMetricsRepository.existsById(id)) {
            bodyMetricsRepository.deleteById(id);
            return "Body metric entry with ID " + id + " was successfully deleted.";
        }
        return "Entry not found with ID " + id;
    }

    @PutMapping("/body-metrics/{id}")
    public BodyMetrics updateBodyMetrics(@PathVariable Long id, @RequestBody BodyMetrics updatedMetrics) {
        return bodyMetricsRepository.findById(id).map(existing -> {
            if (updatedMetrics.getWeight() != null) existing.setWeight(updatedMetrics.getWeight());
            if (updatedMetrics.getMuscleMass() != null) existing.setMuscleMass(updatedMetrics.getMuscleMass());
            if (updatedMetrics.getFatPercentage() != null) existing.setFatPercentage(updatedMetrics.getFatPercentage());
            return bodyMetricsRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Body metric entry not found with ID " + id));
    }



    @PostMapping("/exercises")
    public Exercise createExercise(@RequestBody Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @PostMapping("/workouts/log")
    public WorkoutLog logWorkout(@RequestBody WorkoutLog log) {
        // This expects the incoming JSON to include an existing exercise ID link
        return workoutLogRepository.save(log);
    }

    @GetMapping("/workouts/progress/{exerciseId}")
    public List<WorkoutLog> getExerciseProgress(@PathVariable Long exerciseId) {
        return workoutLogRepository.findByExerciseIdOrderByLogDateAsc(exerciseId);
    }
    @GetMapping("/exercises")
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    @DeleteMapping("/workouts/log/{id}")
    public String deleteWorkoutLog(@PathVariable Long id) {
        if (workoutLogRepository.existsById(id)) {
            workoutLogRepository.deleteById(id);
            return "Workout log with ID " + id + " was successfully deleted.";
        }
        return "Workout log not found with ID " + id;
    }

    @PutMapping("/workouts/log/{id}")
    public WorkoutLog updateWorkoutLog(@PathVariable Long id, @RequestBody WorkoutLog updatedLog) {
        return workoutLogRepository.findById(id).map(existing -> {
            if (updatedLog.getWeightLifted() != null) existing.setWeightLifted(updatedLog.getWeightLifted());
            if (updatedLog.getReps() != null) existing.setReps(updatedLog.getReps());
            if (updatedLog.getSets() != null) existing.setSets(updatedLog.getSets());
            return workoutLogRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Workout log not found with ID " + id));
    }


    @DeleteMapping("/exercises/{id}")
    public String deleteExercise(@PathVariable Long id) {
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
            return "Exercise with ID " + id + " and all associated logs were deleted.";
        }
        return "Exercise not found with ID " + id;
    }


    @PostMapping("/crowd/report")
    public CrowdLog reportCrowd(@RequestBody CrowdLog log) {
        return crowdLogRepository.save(log);
    }

    @GetMapping("/crowd/stats/{day}")
    public List<CrowdLog> getCrowdStatsByDay(@PathVariable String day) {
        return crowdLogRepository.findByDayOfWeek(day);
    }

    @DeleteMapping("/crowd/report/{id}")
    public String deleteCrowdReport(@PathVariable Long id) {
        if (crowdLogRepository.existsById(id)) {
            crowdLogRepository.deleteById(id);
            return "Crowd report with ID " + id + " was successfully deleted.";
        }
        return "Crowd report not found with ID " + id;
    }

    @PutMapping("/crowd/report/{id}")
    public CrowdLog updateCrowdReport(@PathVariable Long id, @RequestBody CrowdLog updatedReport) {
        return crowdLogRepository.findById(id).map(existing -> {
            if (updatedReport.getCrowdLevel() != null) existing.setCrowdLevel(updatedReport.getCrowdLevel());
            if (updatedReport.getTimeSlot() != null) existing.setTimeSlot(updatedReport.getTimeSlot());
            if (updatedReport.getDayOfWeek() != null) existing.setDayOfWeek(updatedReport.getDayOfWeek());
            return crowdLogRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Crowd log not found with ID " + id));
    }

    @GetMapping("/analytics/optimal-slots/{day}")
    public List<SlotSuggestionDTO> getOptimalTimeSlots(@PathVariable String day) {
        List<Object[]> rawResults = crowdLogRepository.findOptimalTimeSlots(day);
        return rawResults.stream()
                .map(result -> new SlotSuggestionDTO((String) result[0], (Double) result[1]))
                .toList();
    }



}
