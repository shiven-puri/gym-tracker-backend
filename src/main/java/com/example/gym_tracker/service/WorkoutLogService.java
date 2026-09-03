package com.example.gym_tracker.service;

import com.example.gym_tracker.model.WorkoutLog;
import com.example.gym_tracker.repository.WorkoutLogRepository;
import org.springframework.stereotype.Service;
import com.example.gym_tracker.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class WorkoutLogService {

    private final WorkoutLogRepository workoutLogRepository;

    public WorkoutLogService(WorkoutLogRepository workoutLogRepository) {
        this.workoutLogRepository = workoutLogRepository;
    }

    public WorkoutLog logWorkout(WorkoutLog log) {
        return workoutLogRepository.save(log);
    }

    public List<WorkoutLog> getExerciseProgress(Long exerciseId) {
        return workoutLogRepository
                .findByExerciseIdOrderByLogDateAsc(exerciseId);
    }

    public WorkoutLog updateWorkoutLog(
            Long id,
            WorkoutLog updatedLog
    ) {
        return workoutLogRepository.findById(id).map(existing -> {

            if (updatedLog.getWeightLifted() != null) {
                existing.setWeightLifted(updatedLog.getWeightLifted());
            }

            if (updatedLog.getReps() != null) {
                existing.setReps(updatedLog.getReps());
            }

            if (updatedLog.getSets() != null) {
                existing.setSets(updatedLog.getSets());
            }

            return workoutLogRepository.save(existing);

        }).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Workout log not found with ID " + id
                )
        );
    }

    public void deleteWorkoutLog(Long id) {

        workoutLogRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workout log not found with ID " + id
                        )
                );

        workoutLogRepository.deleteById(id);
    }
}