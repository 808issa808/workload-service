package org.epam.data;

import org.epam.model.TrainerSummary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Storage implements SummaryRepo {
    private final Map<String, TrainerSummary> trainerSummaryRepo = new ConcurrentHashMap<>();
    @Override
    public TrainerSummary get(String username) {
        return trainerSummaryRepo.get(username);
    }
    @Override
    public TrainerSummary getOrCreate(String username) {
        return trainerSummaryRepo.computeIfAbsent(username, u -> {
            TrainerSummary ts = new TrainerSummary();
            ts.setTrainerUsername(u);
            return ts;
        });
    }

    @Override
    public TrainerSummary save(String username, TrainerSummary trainerSummary) {
        trainerSummaryRepo.put(username, trainerSummary);
        return trainerSummary;
    }
}
