package org.epam.model;

import lombok.Data;
import org.epam.web.dto.UpdateReport;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Data
public class TrainerSummary {
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private boolean isActive;
    private List<YearSummary> yearSummaries;

    public void applyReport(UpdateReport report) {
        this.trainerFirstname = report.getFirstname();
        this.trainerLastname = report.getLastname();
        this.isActive = report.isActive();

        if (yearSummaries == null) {
            yearSummaries = new ArrayList<>();
        }

        int year = report.getTrainingDate().getYear();
        Month month = report.getTrainingDate().getMonth();

        YearSummary yearSummary = yearSummaries.stream()
                .filter(ys -> ys.getYear() == year)
                .findFirst()
                .orElseGet(() -> {
                    YearSummary ys = new YearSummary(year);
                    yearSummaries.add(ys);
                    return ys;
                });

        yearSummary.updateMonth(month, report.getDuration(), report.getAction());
    }
}