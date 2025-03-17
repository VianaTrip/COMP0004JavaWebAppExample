package uk.ac.ucl.model;

import java.time.LocalDateTime;

/**
 * A note that contains an image reference and optional description.
 */
public class ImageNote extends Note {
    private String imagePath;
    private String description;

    /**
     * Creates a new image note.
     *
     * @param title The title for the note
     * @param imagePath The path to the image file
     * @param description A description of the image
     */
    public ImageNote(String title, String imagePath, String description) {
        super(title);
        this.imagePath = imagePath != null ? imagePath : "";
        this.description = description != null ? description : "";
    }

    /**
     * Creates an image note from stored data.
     *
     * @param id The note's unique identifier
     * @param title The title for the note
     * @param imagePath The path to the image file
     * @param description A description of the image
     * @param createdAt When the note was created
     * @param updatedAt When the note was last updated
     */
    public ImageNote(String id, String title, String imagePath, String description,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, title, createdAt, updatedAt);
        this.imagePath = imagePath != null ? imagePath : "";
        this.description = description != null ? description : "";
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath != null ? imagePath : "";
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
        return imagePath + (description.isEmpty() ? "" : "\n\n" + description);
    }

    @Override
    public String getSearchableText() {
        return description;
    }

    @Override
    public String getType() {
        return "IMAGE";
    }
}