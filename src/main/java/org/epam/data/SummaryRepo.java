package org.epam.data;

import org.epam.model.TrainerSummary;
import java.util.Optional;

public interface SummaryRepo {
    Optional<TrainerSummary> get(String username);
    TrainerSummary save(String username, TrainerSummary trainerSummary);
}