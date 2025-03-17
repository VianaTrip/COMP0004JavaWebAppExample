package uk.ac.ucl.model;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The main model class for the notes application.
 * Manages all notes, categories, and operations.
 */
public class Model {
  private final NoteIndex noteIndex;
  private NoteStorage storage;
  private boolean initialized = false;

  /**
   * Creates a new model with an empty note index.
   */
  public Model() {
    noteIndex = new NoteIndex();
  }

  /**
   * Initializes the model by loading all notes and categories from storage.
   *
   * @param storage The storage implementation to use
   * @throws IOException If there is an error loading data from storage
   */
  public void initialize(NoteStorage storage) throws IOException {
    if (initialized) {
      return;
    }

    this.storage = storage;

    // Load all notes and categories from storage
    List<Note> notes = storage.loadAllNotes();
    for (Note note : notes) {
      noteIndex.addNote(note);
    }

    List<Category> categories = storage.loadAllCategories();
    for (Category category : categories) {
      noteIndex.addCategory(category);
    }

    initialized = true;
  }

  /**
   * Creates a new text note.
   *
   * @param title The title of the note
   * @param text The text content of the note
   * @return The created note
   * @throws IOException If there is an error saving the note
   */
  public TextNote createTextNote(String title, String text) throws IOException {
    TextNote note = new TextNote(title, text);
    noteIndex.addNote(note);
    storage.saveNote(note);
    return note;
  }

  /**
   * Creates a new URL note.
   *
   * @param title The title of the note
   * @param url The URL
   * @param description An optional description
   * @return The created note
   * @throws IOException If there is an error saving the note
   */
  public URLNote createURLNote(String title, String url, String description) throws IOException {
    URLNote note = new URLNote(title, url, description);
    noteIndex.addNote(note);
    storage.saveNote(note);
    return note;
  }

  /**
   * Creates a new image note.
   *
   * @param title The title of the note
   * @param imagePath The path to the stored image
   * @param description An optional description
   * @return The created note
   * @throws IOException If there is an error saving the note
   */
  public ImageNote createImageNote(String title, String imagePath, String description) throws IOException {
    ImageNote note = new ImageNote(title, imagePath, description);
    noteIndex.addNote(note);
    storage.saveNote(note);
    return note;
  }

  /**
   * Gets a note by its ID.
   *
   * @param noteId The ID of the note to get
   * @return The note, or null if no note with that ID exists
   */
  public Note getNote(String noteId) {
    return noteIndex.getNote(noteId);
  }

  /**
   * Updates a text note.
   *
   * @param noteId The ID of the note to update
   * @param title The new title
   * @param text The new text content
   * @return true if the note was updated, false if the note doesn't exist or isn't a TextNote
   * @throws IOException If there is an error saving the note
   */
  public boolean updateTextNote(String noteId, String title, String text) throws IOException {
    Note note = noteIndex.getNote(noteId);
    if (note == null || !(note instanceof TextNote)) {
      return false;
    }

    TextNote textNote = (TextNote) note;
    textNote.setTitle(title);
    textNote.setText(text);

    storage.saveNote(textNote);
    return true;
  }

  /**
   * Updates a URL note.
   *
   * @param noteId The ID of the note to update
   * @param title The new title
   * @param url The new URL
   * @param description The new description
   * @return true if the note was updated, false if the note doesn't exist or isn't a URLNote
   * @throws IOException If there is an error saving the note
   */
  public boolean updateURLNote(String noteId, String title, String url, String description) throws IOException {
    Note note = noteIndex.getNote(noteId);
    if (note == null || !(note instanceof URLNote)) {
      return false;
    }

    URLNote urlNote = (URLNote) note;
    urlNote.setTitle(title);
    urlNote.setUrl(url);
    urlNote.setDescription(description);

    storage.saveNote(urlNote);
    return true;
  }

  /**
   * Updates an image note.
   *
   * @param noteId The ID of the note to update
   * @param title The new title
   * @param imagePath The new image path
   * @param description The new description
   * @return true if the note was updated, false if the note doesn't exist or isn't an ImageNote
   * @throws IOException If there is an error saving the note
   */
  public boolean updateImageNote(String noteId, String title, String imagePath, String description) throws IOException {
    Note note = noteIndex.getNote(noteId);
    if (note == null || !(note instanceof ImageNote)) {
      return false;
    }

    ImageNote imageNote = (ImageNote) note;
    imageNote.setTitle(title);
    imageNote.setImagePath(imagePath);
    imageNote.setDescription(description);

    storage.saveNote(imageNote);
    return true;
  }

  /**
   * Deletes a note.
   *
   * @param noteId The ID of the note to delete
   * @return true if the note was deleted, false if it didn't exist
   * @throws IOException If there is an error deleting the note
   */
  public boolean deleteNote(String noteId) throws IOException {
    boolean removed = noteIndex.removeNote(noteId);
    if (removed) {
      storage.deleteNote(noteId);
    }
    return removed;
  }

  /**
   * Gets all notes.
   *
   * @return A list of all notes
   */
  public List<Note> getAllNotes() {
    return noteIndex.getAllNotes();
  }

  /**
   * Gets all note titles.
   *
   * @return A list of all note titles
   */
  public List<String> getAllNoteTitles() {
    return noteIndex.getAllNotes().stream()
            .map(Note::getTitle)
            .collect(Collectors.toList());
  }

  /**
   * Searches for notes matching the given query.
   *
   * @param query The search query
   * @return A list of notes matching the query
   */
  public List<Note> searchNotes(String query) {
    return noteIndex.searchNotes(query);
  }

  /**
   * Creates a new category.
   *
   * @param name The name of the category
   * @return The created category
   * @throws IOException If there is an error saving the category
   */
  public Category createCategory(String name) throws IOException {
    Category category = new Category(name);
    noteIndex.addCategory(category);
    storage.saveCategory(category);
    return category;
  }

  /**
   * Gets a category by its ID.
   *
   * @param categoryId The ID of the category to get
   * @return The category, or null if no category with that ID exists
   */
  public Category getCategory(String categoryId) {
    return noteIndex.getCategory(categoryId);
  }

  /**
   * Gets all categories.
   *
   * @return A list of all categories
   */
  public List<Category> getAllCategories() {
    return noteIndex.getAllCategories();
  }

  /**
   * Gets all category names.
   *
   * @return A list of all category names
   */
  public List<String> getAllCategoryNames() {
    return noteIndex.getAllCategories().stream()
            .map(Category::getName)
            .collect(Collectors.toList());
  }

  /**
   * Updates a category.
   *
   * @param categoryId The ID of the category to update
   * @param name The new name
   * @return true if the category was updated, false if it didn't exist
   * @throws IOException If there is an error saving the category
   */
  public boolean updateCategory(String categoryId, String name) throws IOException {
    Category category = noteIndex.getCategory(categoryId);
    if (category == null) {
      return false;
    }

    category.setName(name);
    storage.saveCategory(category);
    return true;
  }

  /**
   * Deletes a category.
   *
   * @param categoryId The ID of the category to delete
   * @return true if the category was deleted, false if it didn't exist
   * @throws IOException If there is an error deleting the category
   */
  public boolean deleteCategory(String categoryId) throws IOException {
    boolean removed = noteIndex.removeCategory(categoryId);
    if (removed) {
      storage.deleteCategory(categoryId);
    }
    return removed;
  }

  /**
   * Adds a note to a category.
   *
   * @param noteId The ID of the note to add
   * @param categoryId The ID of the category to add the note to
   * @return true if the note was added, false otherwise
   * @throws IOException If there is an error saving the category
   */
  public boolean addNoteToCategory(String noteId, String categoryId) throws IOException {
    boolean added = noteIndex.addNoteToCategory(noteId, categoryId);
    if (added) {
      // Save the updated category
      Category category = noteIndex.getCategory(categoryId);
      storage.saveCategory(category);
    }
    return added;
  }

  /**
   * Removes a note from a category.
   *
   * @param noteId The ID of the note to remove
   * @param categoryId The ID of the category to remove the note from
   * @return true if the note was removed, false otherwise
   * @throws IOException If there is an error saving the category
   */
  public boolean removeNoteFromCategory(String noteId, String categoryId) throws IOException {
    boolean removed = noteIndex.removeNoteFromCategory(noteId, categoryId);
    if (removed) {
      // Save the updated category
      Category category = noteIndex.getCategory(categoryId);
      storage.saveCategory(category);
    }
    return removed;
  }

  /**
   * Gets the notes in a category.
   *
   * @param categoryId The ID of the category
   * @return A list of notes in the category
   */
  public List<Note> getNotesInCategory(String categoryId) {
    return noteIndex.getNotesInCategory(categoryId);
  }

  /**
   * Gets the categories that a note belongs to.
   *
   * @param noteId The ID of the note
   * @return List of categories the note belongs to
   */
  public List<Category> getCategoriesForNote(String noteId) {
    return noteIndex.getCategoriesForNote(noteId);
  }

  /**
   * This method is kept for backward compatibility with the original example.
   * It should not be used in the new application.
   */
  @Deprecated
  public void readFile(String fileName) {
    // This method is kept only for backward compatibility
  }

  /**
   * This method is kept for backward compatibility with the original example.
   * It returns an empty list and should not be used.
   */
  @Deprecated
  public List<String> getPatientNames() {
    // This method is kept only for backward compatibility
    return List.of();
  }

  /**
   * This method is kept for backward compatibility with the original example.
   * It should not be used in the new application.
   */
  @Deprecated
  public List<String> searchFor(String keyword) {
    // This method is kept only for backward compatibility
    return List.of();
  }
}