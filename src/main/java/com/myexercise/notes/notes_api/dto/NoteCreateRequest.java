package com.myexercise.notes.notes_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NoteCreateRequest(
        @NotNull @Min(1) Integer patientId,
        @NotBlank String author,
        @NotBlank @Size(max = 150) String text) {}
