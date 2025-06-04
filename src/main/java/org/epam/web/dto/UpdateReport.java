package org.epam.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateReport {
    @NotBlank
    private String username;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotNull
    private boolean isActive;
    @NotNull
    private LocalDate trainingDate;
    @NotNull
    @PositiveOrZero
    private Integer duration;
    @NotNull
    private Action action;

    public enum Action
    {
        ADD,DELETE
    }
}
