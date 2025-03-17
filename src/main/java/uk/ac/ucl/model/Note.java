package uk.ac.ucl.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The base class for all types of notes in the application.
 * Provides common functionality and properties for all note types.
 */
public abstract class Note {
    private final String id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Creates a new note with a specified title.
     *
     * @param title The title for the note
     */
    public Note(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    /**
     * Creates a note with an existing ID (for loading from storage).
     *
     * @param id The note's unique identifier
     * @param title The title for the note
     * @param createdAt When the note was created
     * @param updatedAt When the note was last updated
     */
    public Note(String id, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    protected void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Returns the note content for display.
     * Each note type will implement this differently.
     *
     * @return The displayable content of the note
     */
    public abstract String getContent();

    /**
     * Returns a plain text version of the note's content for searching.
     *
     * @return The searchable text of the note
     */
    public abstract String getSearchableText();

    /**
     * Returns the type of note as a string.
     *
     * @return The note type identifier
     */
    public abstract String getType();

    /**
     * Method to check if this note matches a search query.
     *
     * @param query The search query
     * @return true if the note matches the query, false otherwise
     */
    public boolean matches(String query) {
        if (query == null || query.isEmpty()) {
            return false;
        }

        String lowerQuery = query.toLowerCase();
        return title.toLowerCase().contains(lowerQuery) ||
                getSearchableText().toLowerCase().contains(lowerQuery);
    }
}