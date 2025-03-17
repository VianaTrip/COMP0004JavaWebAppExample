package uk.ac.ucl.model;

import java.io.IOException;
import java.util.List;

/**
 * Interface for storing and retrieving notes and categories.
 */
public interface NoteStorage {

    /**
     * Saves a note.
     *
     * @param note The note to save
     * @throws IOException If there is an error saving the note
     */
    void saveNote(Note note) throws IOException;

    /**
     * Deletes a note.
     *
     * @param noteId The ID of the note to delete
     * @throws IOException If there is an error deleting the note
     */
    void deleteNote(String noteId) throws IOException;

    /**
     * Loads all notes.
     *
     * @return A list of all notes
     * @throws IOException If there is an error loading the notes
     */
    List<Note> loadAllNotes() throws IOException;

    /**
     * Saves a category.
     *
     * @param category The category to save
     * @throws IOException If there is an error saving the category
     */
    void saveCategory(Category category) throws IOException;

    /**
     * Deletes a category.
     *
     * @param categoryId The ID of the category to delete
     * @throws IOException If there is an error deleting the category
     */
    void deleteCategory(String categoryId) throws IOException;

    /**
     * Loads all categories.
     *
     * @return A list of all categories
     * @throws IOException If there is an error loading the categories
     */
    List<Category> loadAllCategories() throws IOException;
}