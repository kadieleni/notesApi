package com.myexercise.notes.notes_api.service;

import org.springframework.stereotype.Service;

import com.myexercise.notes.notes_api.dto.NoteCreateRequest;
import com.myexercise.notes.notes_api.dto.NoteUpdateRequest;
import com.myexercise.notes.notes_api.entity.Note;
import com.myexercise.notes.notes_api.exception.ForbiddenActionException;
import com.myexercise.notes.notes_api.exception.NoteNotFoundException;
import com.myexercise.notes.notes_api.tenant.TenantContext;
import com.myexercise.notes.notes_api.repository.NotesRepository;

import java.util.List;

@Service
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepository;

    public NotesServiceImpl(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @Override
    public List<Note> getNotesByPatientId(Integer patientId) {
        return getPatientNotesIfExistForTenant(patientId);
    }

    @Override
    public Note updateNote(Long id, NoteUpdateRequest request) {
        Note note = getNoteIfExistsForTenant(id);
        note.setAuthor(request.author());
        note.setText(request.text());
        return notesRepository.save(note);
    }

    @Override
    public Note createNote(NoteCreateRequest request) {
        Note note = new Note(TenantContext.getCurrentTenant(), request.patientId(), request.author(), request.text());
        return notesRepository.save(note);
    }

    @Override
    public void deleteNote(Long id) {
        getNoteIfExistsForTenant(id);
        notesRepository.deleteById(id);
    }

    private Note getNoteIfExistsForTenant(Long id) {
        Note note = getNoteIfExists(id);
        if (!isAuthorised(note)) {
            throw new ForbiddenActionException(id);
        }
        return note;
    }

    private Note getNoteIfExists(Long id){
        return notesRepository.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
    }

    private List<Note> getPatientNotesIfExistForTenant(Integer patientId) {
        List<Note> notes = notesRepository.findByPatientId(patientId);
        if (notes.isEmpty()) {
            throw new NoteNotFoundException(patientId);
        }
        List<Note> filteredNotes = notes.stream().filter(this::isAuthorised).toList();
        if (filteredNotes.isEmpty()) {
            throw new ForbiddenActionException(patientId);
        }
        return filteredNotes;
    }

    private boolean isAuthorised(Note note) {
        return TenantContext.getCurrentTenant().equals(note.getTenantId());
    }

}
