package com.myexercise.notes.notes_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import com.myexercise.notes.notes_api.dto.NoteUpdateRequest;
import com.myexercise.notes.notes_api.entity.Note;
import com.myexercise.notes.notes_api.dto.NoteCreateRequest;
import com.myexercise.notes.notes_api.service.NotesService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/notes")
@Tag(name = "Notes API", description = "CRUD operations for Notes")
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @Operation(summary = "Create a new note")
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody @Valid NoteCreateRequest note) {
        return ResponseEntity.ok(notesService.createNote(note));
    }

    @Operation(summary = "Get notes by Patient ID")
    @GetMapping(params = "patientId")
    public ResponseEntity<List<Note>> getNoteById(@RequestParam @NotNull @Min(1) Integer patientId) {
        return ResponseEntity.ok(notesService.getNotesByPatientId(patientId));
    }

    @Operation(summary = "Update a note by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody @Valid NoteUpdateRequest noteDetails) {
        return ResponseEntity.ok(notesService.updateNote(id, noteDetails));
    }

    @Operation(summary = "Delete a note by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        notesService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

}
