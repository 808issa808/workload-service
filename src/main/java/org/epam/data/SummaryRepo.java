package org.epam.data;

import org.epam.model.TrainerSummary;

public interface SummaryRepo {
    TrainerSummary get(String username);
    TrainerSummary getOrCreate(String username);
    TrainerSummary save(String username, TrainerSummary trainerSummary);
}