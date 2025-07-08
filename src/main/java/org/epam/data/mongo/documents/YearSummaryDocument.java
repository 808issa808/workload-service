package org.epam.data.mongo.documents;

import lombok.Data;

import java.util.List;

@Data
public class YearSummaryDocument {
    private int year;
    private List<MonthSummaryDocument> monthSummaries;
}
