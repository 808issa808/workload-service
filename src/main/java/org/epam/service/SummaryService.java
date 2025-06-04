package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.data.Storage;
import org.epam.model.TrainerSummary;
import org.epam.web.dto.UpdateReport;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private final Storage summaryRepository;
    public void update(UpdateReport updateReport) {
        var username = updateReport.getUsername();
        var localDate = updateReport.getTrainingDate();
        int year = localDate.getYear();
        Month month = localDate.getMonth();
        TrainerSummary summary = summaryRepository.getTrainerSummaryRepo()
                .computeIfAbsent(username, k -> {
                    TrainerSummary ts = new TrainerSummary();
                    ts.setTrainerUsername(username);
                    ts.setSummaryMap(new HashMap<>());
                    return ts;
                });

        summary.setTrainerFirstname(updateReport.getFirstname());
        summary.setTrainerLastname(updateReport.getLastname());
        summary.setActive(updateReport.isActive());

        Map<Month, Integer> monthMap = summary.getSummaryMap()
                .computeIfAbsent(year, y -> new HashMap<>());

        monthMap.compute(month, (m, duration) -> {
            int current = duration != null ? duration : 0;
            return updateReport.getAction() == UpdateReport.Action.ADD
                    ? current + updateReport.getDuration()
                    : Math.max(0, current - updateReport.getDuration());
        });
        System.out.println(summary);
    }


    public TrainerSummary findByUsername(String username) {
        return summaryRepository.getTrainerSummaryRepo().get(username);
    }
}