package com.myexercise.notes.notes_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NoteUpdateRequest(
        @NotBlank String author,
        @NotBlank @Size(max = 150) String text) {

}
