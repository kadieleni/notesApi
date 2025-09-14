package com.myexercise.notes.notes_api.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String tenantId;
    @NotNull
    @Min(value = 1, message = "Patient ID must be greater than 0")
    private Integer patientId;
    @NotBlank
    private String author;
    @NotBlank(message = "Text cannot be blank")
    @Size(max = 150, message = "Text cannot exceed 150 characters")
    private String text;
    private Instant createdAt;

    public Note() {
    }

    public Note(String tenantId, int patientId, String author, String text) {
        this.tenantId = tenantId;
        this.patientId = patientId;
        this.author = author;
        this.text = text;
        this.createdAt = Instant.now();
    }

    public Note(Long id, String tenantId, int patientId, String author, String text, Instant createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.patientId = patientId;
        this.author = author;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
