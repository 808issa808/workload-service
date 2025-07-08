package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.data.SummaryRepo;
import org.epam.model.TrainerSummary;
import org.epam.web.dto.UpdateReport;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private final SummaryRepo summaryRepository;

    public void update(UpdateReport updateReport) {
        var username = updateReport.getUsername();
        TrainerSummary summary = summaryRepository.get(username)
                .orElseGet(() -> {
                    TrainerSummary ts = new TrainerSummary();
                    ts.setTrainerUsername(username);
                    return ts;
                });
        summary.applyReport(updateReport);
        summaryRepository.save(username, summary);
    }

    public Optional<TrainerSummary> findByUsername(String username) {
        return summaryRepository.get(username);
    }
}