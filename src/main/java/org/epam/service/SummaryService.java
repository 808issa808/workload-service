package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.data.SummaryRepo;
import org.epam.model.TrainerSummary;
import org.epam.web.dto.UpdateReport;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private final SummaryRepo summaryRepository;

    public void update(UpdateReport updateReport) {
        var username = updateReport.getUsername();
        TrainerSummary summary = summaryRepository.getOrCreate(username);
        summary.applyReport(updateReport);
        summaryRepository.save(username,summary);
    }

    public TrainerSummary findByUsername(String username) {
        return summaryRepository.get(username);
    }
}