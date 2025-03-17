package uk.ac.ucl.model;

import java.time.LocalDateTime;

/**
 * A note that stores a reference to an image with an optional caption.
 */
public class ImageNote extends Note {
    private String imagePath;
    private String caption;

    /**
     * Creates a new image note.
     *
     * @param title The title for the note
     * @param imagePath Path to the stored image
     * @param caption An optional caption (can be null)
     */
    public ImageNote(String title, String imagePath, String caption) {
        super(title);
        this.imagePath = imagePath != null ? imagePath : "";
        this.caption = caption != null ? caption : "";
    }

    /**
     * Creates an image note from stored data.
     *
     * @param id The note's unique identifier
     * @param title The title for the note
     * @param imagePath Path to the stored image
     * @param caption An optional caption
     * @param createdAt When the note was created
     * @param updatedAt When the note was last updated
     */
    public ImageNote(String id, String title, String imagePath, String caption,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, title, createdAt, updatedAt);
        this.imagePath = imagePath != null ? imagePath : "";
        this.caption = caption != null ? caption : "";
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath != null ? imagePath : "";
        updateTimestamp();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption != null ? caption : "";
        updateTimestamp();
    }

    @Override
    public String getContent() {
        return imagePath + (caption.isEmpty() ? "" : "\n\n" + caption);
    }

    @Override
    public String getSearchableText() {
        return caption; // Since image path isn't really searchable text
    }

    @Override
    public String getType() {
        return "IMAGE";
    }
}