package org.epam.data.mongo;

import org.epam.data.mongo.documents.TrainerSummaryDocument;
import org.epam.model.TrainerSummary;
import org.mapstruct.Mapper;

import java.time.Month;

@Mapper(componentModel = "spring")
public interface TrainerSummaryMapper {
    TrainerSummaryDocument toDocument(TrainerSummary domain);
    TrainerSummary toDomain(TrainerSummaryDocument document);

    default String mapMonth(Month month) {
        return month.name();
    }

    default Month mapMonth(String name) {
        return Month.valueOf(name);
    }
}
