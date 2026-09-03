package com.example.gym_tracker.service;

import com.example.gym_tracker.model.BodyMetrics;
import com.example.gym_tracker.repository.BodyMetricsRepository;
import org.springframework.stereotype.Service;
import com.example.gym_tracker.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class BodyMetricsService {

    private final BodyMetricsRepository bodyMetricsRepository;

    public BodyMetricsService(BodyMetricsRepository bodyMetricsRepository) {
        this.bodyMetricsRepository = bodyMetricsRepository;
    }

    public BodyMetrics logBodyMetrics(BodyMetrics metrics) {
        return bodyMetricsRepository.save(metrics);
    }

    public List<BodyMetrics> getBodyMetricsHistory() {
        return bodyMetricsRepository.findAll();
    }

    public BodyMetrics updateBodyMetrics(
            Long id,
            BodyMetrics updatedMetrics
    ) {
        return bodyMetricsRepository.findById(id).map(existing -> {

            if (updatedMetrics.getWeight() != null) {
                existing.setWeight(updatedMetrics.getWeight());
            }

            if (updatedMetrics.getMuscleMass() != null) {
                existing.setMuscleMass(updatedMetrics.getMuscleMass());
            }

            if (updatedMetrics.getFatPercentage() != null) {
                existing.setFatPercentage(updatedMetrics.getFatPercentage());
            }

            return bodyMetricsRepository.save(existing);

        }).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Body metric entry not found with ID " + id
                )
        );
    }

    public void deleteBodyMetrics(Long id) {

        bodyMetricsRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Body metric entry not found with ID " + id
                        )
                );

        bodyMetricsRepository.deleteById(id);
    }
}