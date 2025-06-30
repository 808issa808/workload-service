package org.epam.data.inner;

import org.epam.data.SummaryRepo;
import org.epam.model.TrainerSummary;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

//@Component
public class Storage implements SummaryRepo {
    private final Map<String, TrainerSummary> trainerSummaryRepo = new ConcurrentHashMap<>();
    @Override
    public Optional<TrainerSummary> get(String username) {
        return Optional.ofNullable(trainerSummaryRepo.get(username));
    }
    @Override
    public TrainerSummary save(String username, TrainerSummary trainerSummary) {
        trainerSummaryRepo.put(username, trainerSummary);
        return trainerSummary;
    }
}
