package org.epam.service;

import org.epam.data.SummaryRepo;
import org.epam.model.TrainerSummary;
import org.epam.web.dto.UpdateReport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SummaryServiceTest {

    @Mock
    private SummaryRepo summaryRepository;

    @InjectMocks
    private SummaryService summaryService;

    private final String TEST_USERNAME = "test.user";
    private final LocalDate TEST_DATE = LocalDate.now();

    private UpdateReport createTestReport(UpdateReport.Action action, int duration) {
        UpdateReport report = new UpdateReport();
        report.setUsername(TEST_USERNAME);
        report.setFirstname("Test");
        report.setLastname("User");
        report.setActive(true);
        report.setTrainingDate(TEST_DATE);
        report.setDuration(duration);
        report.setAction(action);
        return report;
    }

    @Test
    void update_shouldCreateNewSummaryAndApplyReport_whenNotExists() {
        // Arrange
        UpdateReport report = createTestReport(UpdateReport.Action.ADD, 60);
        TrainerSummary newSummary = new TrainerSummary();
        newSummary.setTrainerUsername(TEST_USERNAME);

        when(summaryRepository.getOrCreate(TEST_USERNAME)).thenReturn(newSummary);
        when(summaryRepository.save(anyString(), any(TrainerSummary.class))).thenReturn(newSummary);

        // Act
        summaryService.update(report);

        // Assert
        verify(summaryRepository).getOrCreate(TEST_USERNAME);
        verify(summaryRepository).save(TEST_USERNAME, newSummary);
        assertThat(newSummary.getTrainerUsername()).isEqualTo(TEST_USERNAME);
    }

    @Test
    void update_shouldUpdateExistingSummaryAndApplyReport_whenExists() {
        // Arrange
        UpdateReport report = createTestReport(UpdateReport.Action.ADD, 45);
        TrainerSummary existingSummary = new TrainerSummary();
        existingSummary.setTrainerUsername(TEST_USERNAME);

        when(summaryRepository.getOrCreate(TEST_USERNAME)).thenReturn(existingSummary);
        when(summaryRepository.save(anyString(), any(TrainerSummary.class))).thenReturn(existingSummary);

        // Act
        summaryService.update(report);

        // Assert
        verify(summaryRepository).getOrCreate(TEST_USERNAME);
        verify(summaryRepository).save(TEST_USERNAME, existingSummary);
    }

    @Test
    void findByUsername_shouldReturnSummary_whenExists() {
        // Arrange
        TrainerSummary expectedSummary = new TrainerSummary();
        expectedSummary.setTrainerUsername(TEST_USERNAME);

        when(summaryRepository.get(TEST_USERNAME)).thenReturn(expectedSummary);

        // Act
        TrainerSummary result = summaryService.findByUsername(TEST_USERNAME);

        // Assert
        assertThat(result).isEqualTo(expectedSummary);
        verify(summaryRepository).get(TEST_USERNAME);
    }

    @Test
    void findByUsername_shouldReturnNull_whenNotExists() {
        // Arrange
        when(summaryRepository.get(TEST_USERNAME)).thenReturn(null);

        // Act
        TrainerSummary result = summaryService.findByUsername(TEST_USERNAME);

        // Assert
        assertThat(result).isNull();
        verify(summaryRepository).get(TEST_USERNAME);
    }
}