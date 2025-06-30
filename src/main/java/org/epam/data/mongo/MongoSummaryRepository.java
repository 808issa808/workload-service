package org.epam.data.mongo;

import lombok.RequiredArgsConstructor;
import org.epam.data.SummaryRepo;
import org.epam.model.TrainerSummary;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoSummaryRepository implements SummaryRepo {
    private final SummaryRepository summaryRepository;
    private final TrainerSummaryMapper mapper;

    @Override
    public TrainerSummary get(String username) {
        var found=summaryRepository.findById(username).get();
        return mapper.toDomain(found);
    }

    @Override
    public TrainerSummary getOrCreate(String username) {
        return summaryRepository.findById(username)
                .map(mapper::toDomain)
                .orElseGet(() -> {
                    TrainerSummary ts = new TrainerSummary();
                    ts.setTrainerUsername(username);
                    return ts;
                });
    }

    @Override
    public TrainerSummary save(String username, TrainerSummary trainerSummary) {
        var doc=mapper.toDocument(trainerSummary);
        return mapper.toDomain(summaryRepository.save(doc));
    }
}
