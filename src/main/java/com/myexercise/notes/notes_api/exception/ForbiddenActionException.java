package com.myexercise.notes.notes_api.exception;

public class ForbiddenActionException extends RuntimeException{

    public ForbiddenActionException(Long id) {
        super("Prohibited to manipulate note with id: " + id);
    }

    public ForbiddenActionException(Integer patientId) {
        super("Prohibited to get notes for patient id: " + patientId);
    }

}
