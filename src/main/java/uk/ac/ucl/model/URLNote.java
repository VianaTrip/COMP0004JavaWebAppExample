package uk.ac.ucl.model;

import java.time.LocalDateTime;

/**
 * A note that stores a URL with an optional description.
 */
public class URLNote extends Note {
    private String url;
    private String description;

    /**
     * Creates a new URL note.
     *
     * @param title The title for the note
     * @param url The URL
     * @param description An optional description (can be null)
     */
    public URLNote(String title, String url, String description) {
        super(title);
        this.url = url != null ? url : "";
        this.description = description != null ? description : "";
    }

    /**
     * Creates a URL note from stored data.
     *
     * @param id The note's unique identifier
     * @param title The title for the note
     * @param url The URL
     * @param description An optional description
     * @param createdAt When the note was created
     * @param updatedAt When the note was last updated
     */
    public URLNote(String id, String title, String url, String description,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, title, createdAt, updatedAt);
        this.url = url != null ? url : "";
        this.description = description != null ? description : "";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url != null ? url : "";
        updateTimestamp();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
        updateTimestamp();
    }

    @Override
    public String getContent() {
        return url + (description.isEmpty() ? "" : "\n\n" + description);
    }

    @Override
    public String getSearchableText() {
        return url + " " + description;
    }

    @Override
    public String getType() {
        return "URL";
    }
}