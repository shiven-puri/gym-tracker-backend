
package com.example.gym_tracker.service;

import com.example.gym_tracker.exception.ResourceNotFoundException;
import com.example.gym_tracker.model.User;
import com.example.gym_tracker.model.WorkoutLog;
import com.example.gym_tracker.repository.UserRepository;
import com.example.gym_tracker.repository.WorkoutLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkoutLogService {

    private final WorkoutLogRepository workoutLogRepository;
    private final UserRepository userRepository;

    public WorkoutLogService(
            WorkoutLogRepository workoutLogRepository,
            UserRepository userRepository) {
        this.workoutLogRepository = workoutLogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WorkoutLog logWorkout(WorkoutLog log, Long userId) {
        User user = getUser(userId);
        log.setUser(user);
        log.setId(null);
        return workoutLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<WorkoutLog> getExerciseProgress(
            Long exerciseId, Long userId) {
        return workoutLogRepository
                .findByExerciseIdAndUserIdOrderByLogDateAsc(
                        exerciseId, userId);
    }

    @Transactional(readOnly = true)
    public List<WorkoutLog> getWorkoutLogs(Long userId) {
        return workoutLogRepository.findAllByUserId(userId);
    }

    @Transactional
    public WorkoutLog updateWorkoutLog(
            Long id, WorkoutLog updatedLog, Long userId) {

        WorkoutLog existing = workoutLogRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workout log not found with ID " + id));

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
    }

    @Transactional
    public void deleteWorkoutLog(Long id, Long userId) {
        WorkoutLog existing = workoutLogRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workout log not found with ID " + id));

        workoutLogRepository.delete(existing);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID " + userId));
    }
}
