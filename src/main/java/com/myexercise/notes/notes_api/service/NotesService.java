package com.myexercise.notes.notes_api.service;

import com.myexercise.notes.notes_api.dto.NoteCreateRequest;
import com.myexercise.notes.notes_api.dto.NoteUpdateRequest;
import com.myexercise.notes.notes_api.entity.Note;

import java.util.List;

public interface NotesService {
    List<Note> getNotesByPatientId(Integer patientId);

    Note updateNote(Long id, NoteUpdateRequest noteDetails);

    Note createNote(NoteCreateRequest note);

    void deleteNote(Long id);
}
