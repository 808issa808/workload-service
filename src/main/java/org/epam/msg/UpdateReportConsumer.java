package org.epam.msg;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.web.dto.UpdateReport;
import org.epam.service.SummaryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateReportConsumer {

    private final SummaryService summaryService;

    @KafkaListener(topics = "trainer-summary-update")
    public void consume(UpdateReport report) {
        try {
            log.info("Received update report for {}", report.getUsername());
            summaryService.update(report);
        } catch (Exception e) {
            log.error("Failed to process message", e);
            throw e;
        }
    }
}
