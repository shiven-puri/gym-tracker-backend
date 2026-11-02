
package com.example.gym_tracker.service;

import com.example.gym_tracker.exception.ResourceNotFoundException;
import com.example.gym_tracker.model.BodyMetrics;
import com.example.gym_tracker.model.User;
import com.example.gym_tracker.repository.BodyMetricsRepository;
import com.example.gym_tracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BodyMetricsService {

    private final BodyMetricsRepository bodyMetricsRepository;
    private final UserRepository userRepository;

    public BodyMetricsService(
            BodyMetricsRepository bodyMetricsRepository,
            UserRepository userRepository) {
        this.bodyMetricsRepository = bodyMetricsRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BodyMetrics logBodyMetrics(BodyMetrics metrics, Long userId) {
        User user = getUser(userId);
        metrics.setUser(user);
        metrics.setId(null);
        return bodyMetricsRepository.save(metrics);
    }

    @Transactional(readOnly = true)
    public List<BodyMetrics> getBodyMetricsHistory(Long userId) {
        return bodyMetricsRepository
                .findAllByUserIdOrderByLogDateDesc(userId);
    }

    @Transactional
    public BodyMetrics updateBodyMetrics(
            Long id, BodyMetrics updatedMetrics, Long userId) {

        BodyMetrics existing = bodyMetricsRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Body metric entry not found with ID " + id));

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
    }

    @Transactional
    public void deleteBodyMetrics(Long id, Long userId) {
        BodyMetrics existing = bodyMetricsRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Body metric entry not found with ID " + id));

        bodyMetricsRepository.delete(existing);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID " + userId));
    }
}
