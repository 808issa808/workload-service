package org.epam.model;

import lombok.Data;
import org.epam.web.dto.UpdateReport;

import java.time.Month;

@Data
public class MonthSummary {
    private Month month;
    private int duration;

    public MonthSummary(Month month) {
        this.month = month;
    }

    public void updateDuration(int delta, UpdateReport.Action action) {
        if (action == UpdateReport.Action.ADD) {
            this.duration += delta;
        } else {
            this.duration = Math.max(0, this.duration - delta);
        }
    }
}
