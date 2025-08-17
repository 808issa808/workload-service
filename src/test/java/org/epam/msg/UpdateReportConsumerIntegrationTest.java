package org.epam.msg;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.epam.service.SummaryService;
import org.epam.web.dto.UpdateReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.testcontainers.utility.DockerImageName;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.timeout;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Testcontainers
@SpringBootTest
class UpdateReportConsumerIntegrationTest {
    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @DynamicPropertySource
    static void configureKafka(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    private KafkaTemplate<String, UpdateReport> kafkaTemplate;
    @SpyBean
    private SummaryService summaryService;

    @BeforeEach
    void setup() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        ProducerFactory<String, UpdateReport> factory =
                new DefaultKafkaProducerFactory<>(configs);

        kafkaTemplate = new KafkaTemplate<>(factory);
    }

    @Test
    void shouldConsumeKafkaMessageAndCallSummaryService() throws InterruptedException {
        UpdateReport report = new UpdateReport();
        report.setUsername("test_user");
        report.setFirstname("John");
        report.setLastname("Doe");
        report.setTrainingDate(LocalDate.now());
        report.setDuration(45);
        report.setAction(UpdateReport.Action.ADD);
        report.setActive(true);

        Thread.sleep(2000); // Ждём пока Kafka и consumer полностью прогреются

        kafkaTemplate.send("trainer-summary-update", report);

        // проверяем, что метод был вызван в течение 5 сек
        verify(summaryService, timeout(5000).times(1)).update(report);
    }
}