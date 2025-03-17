package uk.ac.ucl.model;

import java.time.LocalDateTime;

/**
 * A note that contains plain text content.
 */
public class TextNote extends Note {
    private String text;

    /**
     * Creates a new text note.
     *
     * @param title The title for the note
     * @param text The text content
     */
    public TextNote(String title, String text) {
        super(title);
        this.text = text != null ? text : "";
    }

    /**
     * Creates a text note from stored data.
     *
     * @param id The note's unique identifier
     * @param title The title for the note
     * @param text The text content
     * @param createdAt When the note was created
     * @param updatedAt When the note was last updated
     */
    public TextNote(String id, String title, String text, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, title, createdAt, updatedAt);
        this.text = text != null ? text : "";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text != null ? text : "";
        updateTimestamp();
    }

    @Override
    public String getContent() {
        return text;
    }

    @Override
    public String getSearchableText() {
        return text;
    }

    @Override
    public String getType() {
        return "TEXT";
    }
}