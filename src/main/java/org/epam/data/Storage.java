package org.epam.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.epam.model.TrainerSummary;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
public class Storage {
    private final Map<String, TrainerSummary> trainerSummaryRepo;
}
