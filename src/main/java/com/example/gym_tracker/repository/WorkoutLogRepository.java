package com.example.gym_tracker.repository;

import com.example.gym_tracker.model.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {
    List<WorkoutLog> findByExerciseIdOrderByLogDateAsc(Long exerciseId);
}