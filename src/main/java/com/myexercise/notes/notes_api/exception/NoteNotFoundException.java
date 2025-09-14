package com.myexercise.notes.notes_api.exception;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(Long id) {
        super("Note not found with id: " + id);
    }

    public NoteNotFoundException(Integer patientId) {
        super("No Notes found for patient id: " + patientId);
    }
}
