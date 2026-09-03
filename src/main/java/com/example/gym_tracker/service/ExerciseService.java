package com.example.gym_tracker.service;

import com.example.gym_tracker.model.Exercise;
import com.example.gym_tracker.repository.ExerciseRepository;
import com.example.gym_tracker.repository.WorkoutLogRepository;
import org.springframework.stereotype.Service;
import com.example.gym_tracker.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final WorkoutLogRepository workoutLogRepository;

    public ExerciseService(ExerciseRepository exerciseRepository, WorkoutLogRepository workoutLogRepository) {
        this.exerciseRepository = exerciseRepository;
        this.workoutLogRepository = workoutLogRepository;
    }

    public Exercise createExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    @Transactional
    public void deleteExercise(Long id) {
        exerciseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Exercise not found with ID " + id
                        ));

        workoutLogRepository.deleteByExerciseId(id);
        exerciseRepository.deleteById(id);
    }
}