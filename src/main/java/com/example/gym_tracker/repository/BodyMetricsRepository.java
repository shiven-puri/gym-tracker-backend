
package com.example.gym_tracker.repository;

import com.example.gym_tracker.model.BodyMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BodyMetricsRepository
        extends JpaRepository<BodyMetrics, Long> {

    List<BodyMetrics> findAllByUserIdOrderByLogDateDesc(Long userId);

    Optional<BodyMetrics> findByIdAndUserId(Long id, Long userId);
}
