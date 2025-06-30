package org.epam.data.mongo.documents;

import lombok.Data;

@Data
public class MonthSummaryDocument {
    private String month;
    private int duration;
}
