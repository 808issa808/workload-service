package org.epam.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.epam.model.TrainerSummary;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
public class Storage {
    private final Map<String, TrainerSummary> trainerSummaryRepo;

    public TrainerSummary getOrCreate(String username) {
        return trainerSummaryRepo.computeIfAbsent(username, u -> {
            TrainerSummary ts = new TrainerSummary();
            ts.setTrainerUsername(u);
            return ts;
        });
    }

    public TrainerSummary putOrUpdate(String username, TrainerSummary trainerSummary) {
        trainerSummaryRepo.put(username, trainerSummary);
        return trainerSummary;
    }
}
