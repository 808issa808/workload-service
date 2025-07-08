package org.epam.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.epam.model.TrainerSummary;
import org.epam.service.SummaryService;
import org.epam.web.dto.UpdateReport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private final SummaryService summaryService;

    @PostMapping("/trainer-summary")
    public void summaryUpdate(@Valid @RequestBody UpdateReport updateReport) {
        summaryService.update(updateReport);
    }

    @GetMapping("/trainer-summary/{username}")
    public ResponseEntity<TrainerSummary> getSummary(@PathVariable String username) {
        return summaryService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}