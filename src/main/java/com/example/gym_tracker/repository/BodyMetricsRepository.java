package com.example.gym_tracker.repository;

import com.example.gym_tracker.model.BodyMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyMetricsRepository extends JpaRepository<BodyMetrics, Long> {
}
