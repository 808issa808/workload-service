package org.epam.data.mongo;

import org.epam.model.TrainerSummary;
import org.epam.web.dto.UpdateReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({TrainerSummaryMapperImpl.class, MongoSummaryRepository.class})
@Testcontainers
class MongoSummaryRepositoryComponentTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    MongoSummaryRepository mongoSummaryRepository;

    private TrainerSummary trainerSummary;

    @BeforeEach
    void setUp() {
        trainerSummary = new TrainerSummary();
        trainerSummary.setTrainerUsername("trainer1");
        trainerSummary.setTrainerFirstname("John");
        trainerSummary.setTrainerLastname("Doe");
        trainerSummary.setActive(true);
    }

    @Test
    void testSaveAndGet() {
        mongoSummaryRepository.save(trainerSummary.getTrainerUsername(), trainerSummary);

        Optional<TrainerSummary> loaded = mongoSummaryRepository.get("trainer1");

        assertThat(loaded).isPresent();
        assertThat(loaded.get().getTrainerFirstname()).isEqualTo("John");
    }

    @Test
    void testApplyReportAndPersist() {
        UpdateReport report = new UpdateReport();
        report.setFirstname("John");
        report.setLastname("Doe");
        report.setActive(true);
        report.setTrainingDate(LocalDate.of(2025, Month.JULY, 28));
        report.setDuration(90);
        report.setAction(UpdateReport.Action.ADD);

        trainerSummary.applyReport(report);

        mongoSummaryRepository.save("trainer1", trainerSummary);

        Optional<TrainerSummary> result = mongoSummaryRepository.get("trainer1");

        assertThat(result).isPresent();
        TrainerSummary loaded = result.get();

        assertThat(loaded.getYearSummaries()).hasSize(1);
        var yearSummary = loaded.getYearSummaries().get(0);
        assertThat(yearSummary.getYear()).isEqualTo(2025);

        var monthSummary = yearSummary.getMonthSummaries().stream()
                .filter(ms -> ms.getMonth() == Month.JULY)
                .findFirst();

        assertThat(monthSummary).isPresent();
        assertThat(monthSummary.get().getDuration()).isEqualTo(90);
    }

    @Test
    void testSubtractActionCannotGoBelowZero() {
        UpdateReport subtractReport = new UpdateReport();
        subtractReport.setFirstname("John");
        subtractReport.setLastname("Doe");
        subtractReport.setActive(true);
        subtractReport.setTrainingDate(LocalDate.of(2025, Month.JULY, 28));
        subtractReport.setDuration(100);
        subtractReport.setAction(UpdateReport.Action.DELETE);

        trainerSummary.applyReport(subtractReport);

        mongoSummaryRepository.save("trainer1", trainerSummary);

        Optional<TrainerSummary> result = mongoSummaryRepository.get("trainer1");

        assertThat(result).isPresent();
        var monthSummary = result.get().getYearSummaries().get(0).getMonthSummaries().get(0);

        assertThat(monthSummary.getDuration()).isEqualTo(0);
    }
}
