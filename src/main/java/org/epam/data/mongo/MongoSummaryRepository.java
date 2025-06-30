package org.epam.data.mongo;

import lombok.RequiredArgsConstructor;
import org.epam.data.SummaryRepo;
import org.epam.model.TrainerSummary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MongoSummaryRepository implements SummaryRepo {
    private final SummaryRepository summaryRepository;
    private final TrainerSummaryMapper mapper;

    @Override
    public Optional<TrainerSummary> get(String username) {
        return summaryRepository.findById(username)
                .map(mapper::toDomain);
    }

    @Override
    public TrainerSummary save(String username, TrainerSummary trainerSummary) {
        var doc=mapper.toDocument(trainerSummary);
        return mapper.toDomain(summaryRepository.save(doc));
    }
}
