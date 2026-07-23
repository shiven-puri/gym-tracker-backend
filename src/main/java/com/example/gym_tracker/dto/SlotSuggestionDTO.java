package com.example.gym_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SlotSuggestionDTO {
    private String timeSlot;
    private Double averageCrowdLevel;
}