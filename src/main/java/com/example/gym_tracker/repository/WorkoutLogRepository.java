
package com.example.gym_tracker.repository;

import com.example.gym_tracker.model.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutLogRepository
        extends JpaRepository<WorkoutLog, Long> {

    List<WorkoutLog> findByExerciseIdAndUserIdOrderByLogDateAsc(
            Long exerciseId, Long userId);

    List<WorkoutLog> findAllByUserId(Long userId);

    Optional<WorkoutLog> findByIdAndUserId(Long id, Long userId);

    boolean existsByExerciseId(Long exerciseId);

    @Modifying
    @Query("DELETE FROM WorkoutLog w WHERE w.exercise.id = :exerciseId")
    void deleteByExerciseId(@Param("exerciseId") Long exerciseId);
}
