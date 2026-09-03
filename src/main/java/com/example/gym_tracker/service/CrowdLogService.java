package com.example.gym_tracker.service;

import com.example.gym_tracker.dto.SlotSuggestionDTO;
import com.example.gym_tracker.model.CrowdLog;
import com.example.gym_tracker.repository.CrowdLogRepository;
import org.springframework.stereotype.Service;
import com.example.gym_tracker.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class CrowdLogService {

    private final CrowdLogRepository crowdLogRepository;

    public CrowdLogService(CrowdLogRepository crowdLogRepository) {
        this.crowdLogRepository = crowdLogRepository;
    }

    public CrowdLog reportCrowd(CrowdLog log) {
        return crowdLogRepository.save(log);
    }

    public List<CrowdLog> getCrowdStatsByDay(String day) {
        return crowdLogRepository.findByDayOfWeek(day);
    }

    public CrowdLog updateCrowdReport(
            Long id,
            CrowdLog updatedReport
    ) {
        return crowdLogRepository.findById(id).map(existing -> {

            if (updatedReport.getCrowdLevel() != null) {
                existing.setCrowdLevel(updatedReport.getCrowdLevel());
            }

            if (updatedReport.getTimeSlot() != null) {
                existing.setTimeSlot(updatedReport.getTimeSlot());
            }

            if (updatedReport.getDayOfWeek() != null) {
                existing.setDayOfWeek(updatedReport.getDayOfWeek());
            }

            return crowdLogRepository.save(existing);

        }).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Crowd log not found with ID " + id
                )
        );
    }

    public void deleteCrowdReport(Long id) {

        crowdLogRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Crowd report not found with ID " + id
                        )
                );

        crowdLogRepository.deleteById(id);
    }

    public List<SlotSuggestionDTO> getOptimalTimeSlots(String day) {

        List<Object[]> rawResults =
                crowdLogRepository.findOptimalTimeSlots(day);

        return rawResults.stream()
                .map(result ->
                        new SlotSuggestionDTO(
                                (String) result[0],
                                (Double) result[1]
                        )
                )
                .toList();
    }
}