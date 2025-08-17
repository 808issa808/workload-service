package org.epam.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.epam.model.TrainerSummary;
import org.epam.service.SummaryService;
import org.epam.web.dto.UpdateReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiController.class)
class ApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SummaryService summaryService;

    @Autowired
    private ObjectMapper objectMapper;

    private UpdateReport report;

    @BeforeEach
    void setup() {
        report = new UpdateReport();
        report.setUsername("test_user");
        report.setFirstname("John");
        report.setLastname("Doe");
        report.setTrainingDate(LocalDate.now());
        report.setDuration(60);
        report.setAction(UpdateReport.Action.ADD);
        report.setActive(true);
    }

    @Test
    @WithMockUser( roles = {"TRAINER"})
    void shouldReturn200OnValidPost() throws Exception {
        doNothing().when(summaryService).update(report);

        mockMvc.perform(post("/trainer-summary")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(report)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test_user", roles = {"TRAINER"})
    void shouldReturnSummaryIfExists() throws Exception {
        TrainerSummary summary = new TrainerSummary();
        summary.setTrainerUsername("test_user");

        Mockito.when(summaryService.findByUsername("test_user")).thenReturn(Optional.of(summary));

        mockMvc.perform(get("/trainer-summary/test_user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainerUsername").value("test_user"));
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void shouldReturn404IfSummaryNotFound() throws Exception {
        Mockito.when(summaryService.findByUsername("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/trainer-summary/nonexistent"))
                .andExpect(status().isNotFound());
    }
}
