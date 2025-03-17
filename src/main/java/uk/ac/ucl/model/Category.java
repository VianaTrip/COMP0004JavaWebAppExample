package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Represents a category that can contain notes.
 */
public class Category {
    private final String id;
    private String name;
    private final List<String> noteIds; // Store IDs of notes in this category

    /**
     * Creates a new category with the specified name.
     *
     * @param name The category name
     */
    public Category(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.noteIds = new ArrayList<>();
    }

    /**
     * Creates a category from stored data.
     *
     * @param id The category's unique identifier
     * @param name The category name
     * @param noteIds List of note IDs in this category
     */
    public Category(String id, String name, List<String> noteIds) {
        this.id = id;
        this.name = name;
        this.noteIds = new ArrayList<>(noteIds);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNoteIds() {
        return Collections.unmodifiableList(noteIds);
    }

    /**
     * Adds a note to this category.
     *
     * @param noteId The ID of the note to add
     * @return true if the note was added, false if it was already in the category
     */
    public boolean addNote(String noteId) {
        if (!noteIds.contains(noteId)) {
            noteIds.add(noteId);
            return true;
        }
        return false;
    }

    /**
     * Removes a note from this category.
     *
     * @param noteId The ID of the note to remove
     * @return true if the note was removed, false if it wasn't in the category
     */
    public boolean removeNote(String noteId) {
        return noteIds.remove(noteId);
    }

    /**
     * Checks if a note is in this category.
     *
     * @param noteId The ID of the note to check
     * @return true if the note is in this category, false otherwise
     */
    public boolean containsNote(String noteId) {
        return noteIds.contains(noteId);
    }

    /**
     * Returns the number of notes in this category.
     *
     * @return The number of notes
     */
    public int getNoteCount() {
        return noteIds.size();
    }
}