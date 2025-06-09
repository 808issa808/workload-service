package org.epam.model;

import lombok.Data;
import org.epam.web.dto.UpdateReport;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Data
public class YearSummary {
    private int year;
    private List<MonthSummary> monthSummaries;

    public YearSummary(int year) {
        this.year = year;
    }

    public void updateMonth(Month month, int delta, UpdateReport.Action action) {
        if (monthSummaries == null) {
            monthSummaries = new ArrayList<>();
        }

        MonthSummary monthSummary = monthSummaries.stream()
                .filter(ms -> ms.getMonth() == month)
                .findFirst()
                .orElseGet(() -> {
                    MonthSummary ms = new MonthSummary(month);
                    monthSummaries.add(ms);
                    return ms;
                });

        monthSummary.updateDuration(delta, action);
    }
}