package uk.ac.ucl.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The main index for all notes in the application.
 * Handles note storage, retrieval, and searching.
 */
public class NoteIndex {
    private final Map<String, Note> notes;  // Maps note ID to Note object
    private final Map<String, Category> categories;  // Maps category ID to Category object

    /**
     * Creates a new empty note index.
     */
    public NoteIndex() {
        notes = new HashMap<>();
        categories = new HashMap<>();
    }

    /**
     * Adds a note to the index.
     *
     * @param note The note to add
     * @return true if the note was added, false if a note with the same ID already exists
     */
    public boolean addNote(Note note) {
        if (note == null || notes.containsKey(note.getId())) {
            return false;
        }
        notes.put(note.getId(), note);
        return true;
    }

    /**
     * Retrieves a note by its ID.
     *
     * @param id The ID of the note to retrieve
     * @return The note, or null if no note with that ID exists
     */
    public Note getNote(String id) {
        return notes.get(id);
    }

    /**
     * Removes a note from the index.
     * Also removes the note from all categories it belongs to.
     *
     * @param id The ID of the note to remove
     * @return true if the note was removed, false if no note with that ID exists
     */
    public boolean removeNote(String id) {
        if (!notes.containsKey(id)) {
            return false;
        }

        // Remove note from all categories
        for (Category category : categories.values()) {
            category.removeNote(id);
        }

        notes.remove(id);
        return true;
    }

    /**
     * Gets all notes in the index.
     *
     * @return List of all notes
     */
    public List<Note> getAllNotes() {
        return new ArrayList<>(notes.values());
    }

    /**
     * Gets the IDs of all notes in the index.
     *
     * @return List of all note IDs
     */
    public List<String> getAllNoteIds() {
        return new ArrayList<>(notes.keySet());
    }

    /**
     * Gets the number of notes in the index.
     *
     * @return The number of notes
     */
    public int getNoteCount() {
        return notes.size();
    }

    /**
     * Searches for notes matching the given query in their title or content.
     *
     * @param query The search query
     * @return List of notes matching the query
     */
    public List<Note> searchNotes(String query) {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }

        return notes.values().stream()
                .filter(note -> note.matches(query))
                .collect(Collectors.toList());
    }

    /**
     * Adds a category to the index.
     *
     * @param category The category to add
     * @return true if the category was added, false if a category with the same ID already exists
     */
    public boolean addCategory(Category category) {
        if (category == null || categories.containsKey(category.getId())) {
            return false;
        }
        categories.put(category.getId(), category);
        return true;
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to retrieve
     * @return The category, or null if no category with that ID exists
     */
    public Category getCategory(String id) {
        return categories.get(id);
    }

    /**
     * Removes a category from the index.
     * Note: This does not remove the notes that were in the category.
     *
     * @param id The ID of the category to remove
     * @return true if the category was removed, false if no category with that ID exists
     */
    public boolean removeCategory(String id) {
        if (!categories.containsKey(id)) {
            return false;
        }
        categories.remove(id);
        return true;
    }

    /**
     * Gets all categories in the index.
     *
     * @return List of all categories
     */
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories.values());
    }

    /**
     * Gets the notes belonging to a specific category.
     *
     * @param categoryId The ID of the category
     * @return List of notes in the category, or an empty list if the category doesn't exist
     */
    public List<Note> getNotesInCategory(String categoryId) {
        Category category = categories.get(categoryId);
        if (category == null) {
            return new ArrayList<>();
        }

        return category.getNoteIds().stream()
                .map(this::getNote)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Adds a note to a category.
     *
     * @param noteId The ID of the note to add
     * @param categoryId The ID of the category to add the note to
     * @return true if the note was added to the category, false otherwise
     */
    public boolean addNoteToCategory(String noteId, String categoryId) {
        if (!notes.containsKey(noteId) || !categories.containsKey(categoryId)) {
            return false;
        }

        return categories.get(categoryId).addNote(noteId);
    }

    /**
     * Removes a note from a category.
     *
     * @param noteId The ID of the note to remove
     * @param categoryId The ID of the category to remove the note from
     * @return true if the note was removed from the category, false otherwise
     */
    public boolean removeNoteFromCategory(String noteId, String categoryId) {
        if (!categories.containsKey(categoryId)) {
            return false;
        }

        return categories.get(categoryId).removeNote(noteId);
    }

    /**
     * Checks if a note is in a category.
     *
     * @param noteId The ID of the note to check
     * @param categoryId The ID of the category to check
     * @return true if the note is in the category, false otherwise
     */
    public boolean isNoteInCategory(String noteId, String categoryId) {
        if (!categories.containsKey(categoryId)) {
            return false;
        }

        return categories.get(categoryId).containsNote(noteId);
    }

    /**
     * Gets the categories that a note belongs to.
     *
     * @param noteId The ID of the note
     * @return List of categories the note belongs to
     */
    public List<Category> getCategoriesForNote(String noteId) {
        if (!notes.containsKey(noteId)) {
            return new ArrayList<>();
        }

        return categories.values().stream()
                .filter(category -> category.containsNote(noteId))
                .collect(Collectors.toList());
    }
}