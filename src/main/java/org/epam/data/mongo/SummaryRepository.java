package org.epam.data.mongo;

import org.epam.data.mongo.documents.TrainerSummaryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryRepository extends MongoRepository<TrainerSummaryDocument,String> {
}
