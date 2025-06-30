package org.epam.data.mongo;

import org.epam.data.mongo.documents.TrainerSummaryDocument;
import org.epam.model.TrainerSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoSummaryRepositoryTest {

    @Mock
    private SummaryRepository summaryRepository;

    @Mock
    private TrainerSummaryMapper mapper;

    @InjectMocks
    private MongoSummaryRepository mongoSummaryRepository;

    private final String TEST_USERNAME = "test.user";
    private final TrainerSummary TEST_SUMMARY = new TrainerSummary();

    @Test
    void get_shouldReturnSummary_whenExists() {
        // Arrange
        var document = new TrainerSummaryDocument();
        when(summaryRepository.findById(TEST_USERNAME)).thenReturn(Optional.of(document));
        when(mapper.toDomain(document)).thenReturn(TEST_SUMMARY);

        // Act
        TrainerSummary result = mongoSummaryRepository.get(TEST_USERNAME);

        // Assert
        assertThat(result).isEqualTo(TEST_SUMMARY);
        verify(summaryRepository).findById(TEST_USERNAME);
        verify(mapper).toDomain(document);
    }

    @Test
    void getOrCreate_shouldReturnExistingSummary_whenExists() {
        // Arrange
        var document = new TrainerSummaryDocument();
        when(summaryRepository.findById(TEST_USERNAME)).thenReturn(Optional.of(document));
        when(mapper.toDomain(document)).thenReturn(TEST_SUMMARY);

        // Act
        TrainerSummary result = mongoSummaryRepository.getOrCreate(TEST_USERNAME);

        // Assert
        assertThat(result).isEqualTo(TEST_SUMMARY);
        verify(summaryRepository, never()).save(any());
    }

    @Test
    void getOrCreate_shouldCreateNewSummary_whenNotExists() {
        // Arrange
        when(summaryRepository.findById(TEST_USERNAME)).thenReturn(Optional.empty());

        // Act
        TrainerSummary result = mongoSummaryRepository.getOrCreate(TEST_USERNAME);

        // Assert
        assertThat(result.getTrainerUsername()).isEqualTo(TEST_USERNAME);
        assertThat(result).isNotNull();
        verify(summaryRepository, never()).save(any());
    }

    @Test
    void save_shouldMapAndSaveDocument() {
        // Arrange
        var document = new TrainerSummaryDocument();
        when(mapper.toDocument(TEST_SUMMARY)).thenReturn(document);
        when(summaryRepository.save(document)).thenReturn(document);
        when(mapper.toDomain(document)).thenReturn(TEST_SUMMARY);

        // Act
        TrainerSummary result = mongoSummaryRepository.save(TEST_USERNAME, TEST_SUMMARY);

        // Assert
        assertThat(result).isEqualTo(TEST_SUMMARY);
        verify(mapper).toDocument(TEST_SUMMARY);
        verify(summaryRepository).save(document);
        verify(mapper).toDomain(document);
    }
}