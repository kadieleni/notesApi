package com.myexercise.notes.notes_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.myexercise.notes.notes_api.dto.NoteCreateRequest;
import com.myexercise.notes.notes_api.dto.NoteUpdateRequest;
import com.myexercise.notes.notes_api.entity.Note;
import com.myexercise.notes.notes_api.exception.ForbiddenActionException;
import com.myexercise.notes.notes_api.exception.NoteNotFoundException;
import com.myexercise.notes.notes_api.repository.NotesRepository;
import com.myexercise.notes.notes_api.tenant.TenantContext;

import java.util.List;
import java.util.Optional;

public class NotesServiceTest {

    private static final String TENANT_1 = "tenant1";
    private static final String TENANT_2 = "tenant2";
    private static final String TENANT_3 = "tenant3";
    private static final Integer PATIENT_ID = 100;
    private static final Long NOTE_ID = 7L;
    private static final NoteUpdateRequest UPDATE_REQUEST = new NoteUpdateRequest("Andreas", "note text");
    private static final NoteCreateRequest CREATE_REQUEST = new NoteCreateRequest(PATIENT_ID, "Andreas", "note text");

    @Mock
    private NotesRepository notesRepository;
    @Mock
    private Note note1;
    @Mock
    private Note note2;
    @Mock
    private Note note3;

    private Note note4;

    @InjectMocks
    private NotesServiceImpl notesService;

    ArgumentCaptor<Note> noteCaptor = ArgumentCaptor.forClass(Note.class);
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        when(note1.getTenantId()).thenReturn(TENANT_1);
        when(note2.getTenantId()).thenReturn(TENANT_2);
        when(note3.getTenantId()).thenReturn(TENANT_1);
    }

    @Test
    void noNotesForPatientTest() {
        when(notesRepository.findByPatientId(anyInt())).thenReturn(List.of());
        assertThrows(NoteNotFoundException.class, () -> {
            notesService.getNotesByPatientId(PATIENT_ID);
        });
    }

    @Test
    void noPatientNotesForTenantTest() {
        when(notesRepository.findByPatientId(anyInt())).thenReturn(List.of(note1, note2, note3));

        assertThrows(ForbiddenActionException.class, () -> {
            TenantContext.setCurrentTenant(TENANT_3);
            notesService.getNotesByPatientId(PATIENT_ID);
            TenantContext.clear();
        });
    }

    @Test
    void getPatientNotesForTenantTest() {
        when(notesRepository.findByPatientId(anyInt())).thenReturn(List.of(note1, note2, note3));
        TenantContext.setCurrentTenant(TENANT_1);
        List<Note> result = notesService.getNotesByPatientId(PATIENT_ID);

        assertEquals(List.of(note1, note3), result);
        TenantContext.clear();
    }

    @Test
    void noNoteFoundTest() {
        when(notesRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> {
            TenantContext.setCurrentTenant(TENANT_1);
            notesService.updateNote(NOTE_ID, UPDATE_REQUEST);
            TenantContext.clear();
        });
    }

    @Test
    void noNoteForTenantFoundTest() {
        when(notesRepository.findById(anyLong())).thenReturn(Optional.of(note2));

        assertThrows(ForbiddenActionException.class, () -> {
            TenantContext.setCurrentTenant(TENANT_1);
            notesService.updateNote(NOTE_ID, UPDATE_REQUEST);
            TenantContext.clear();
        });
    }

    @Test
    void updateNoteTest() {
        note4 = new Note(TENANT_1, PATIENT_ID, "Eleni", "Old Text");
        when(notesRepository.findById(anyLong())).thenReturn(Optional.of(note4));
        TenantContext.setCurrentTenant(TENANT_1);
        notesService.updateNote(NOTE_ID, UPDATE_REQUEST);
        verify(notesRepository).save(noteCaptor.capture());

        Note note = noteCaptor.getValue();
        assertEquals(UPDATE_REQUEST.author(), note.getAuthor());
        assertEquals(UPDATE_REQUEST.text(), note.getText());

        TenantContext.clear();
    }

    @Test
    void createNoteTest() {
        TenantContext.setCurrentTenant(TENANT_1);
        notesService.createNote(CREATE_REQUEST);
        verify(notesRepository).save(noteCaptor.capture());

        Note note = noteCaptor.getValue();
        assertEquals(TENANT_1, note.getTenantId());
        assertEquals(CREATE_REQUEST.author(), note.getAuthor());
        assertEquals(CREATE_REQUEST.patientId(), note.getPatientId());
        assertEquals(CREATE_REQUEST.text(), note.getText());
        assertNotNull(note.getCreatedAt());

        TenantContext.clear();
    }

    @Test
    void deleteNoteTest() {
        when(notesRepository.findById(anyLong())).thenReturn(Optional.of(note1));
        TenantContext.setCurrentTenant(TENANT_1);
        notesService.deleteNote(NOTE_ID);
        verify(notesRepository).deleteById(idCaptor.capture());

        Long id = idCaptor.getValue();
        assertEquals(NOTE_ID, id);

        TenantContext.clear();
    }

}
