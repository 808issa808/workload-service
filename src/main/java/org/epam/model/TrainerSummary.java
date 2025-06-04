package org.epam.model;

import lombok.Data;

import java.time.Month;
import java.util.Map;

@Data
public class TrainerSummary {
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private boolean isActive;
    private Map<Integer, Map<Month, Integer>> summaryMap;
}
