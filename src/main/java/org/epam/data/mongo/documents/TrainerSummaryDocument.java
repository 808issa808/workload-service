package org.epam.data.mongo.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("trainer_summaries")
@CompoundIndex(name = "name_idx", def = "{'trainerFirstname': 1, 'trainerLastname': 1}")
public class TrainerSummaryDocument {

    @Id
    private String trainerUsername;

    private String trainerFirstname;
    private String trainerLastname;
    private boolean isActive;

    private List<YearSummaryDocument> yearSummaries;
}
