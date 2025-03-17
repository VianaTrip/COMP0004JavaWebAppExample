package uk.ac.ucl.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of NoteStorage that uses JSON files for storage.
 */
public class JsonNoteStorage implements NoteStorage {
    private final String baseDir;
    private final String notesDir;
    private final String categoriesDir;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new JSON storage with the specified base directory.
     *
     * @param baseDir The base directory to store files in
     * @throws IOException If there is an error creating the necessary directories
     */
    public JsonNoteStorage(String baseDir) throws IOException {
        this.baseDir = baseDir;
        this.notesDir = baseDir + "/notes";
        this.categoriesDir = baseDir + "/categories";

        // Create the necessary directories if they don't exist
        Path baseDirectory = Paths.get(baseDir);
        Path notesDirectory = Paths.get(notesDir);
        Path categoriesDirectory = Paths.get(categoriesDir);

        if (!Files.exists(baseDirectory)) {
            Files.createDirectories(baseDirectory);
        }

        if (!Files.exists(notesDirectory)) {
            Files.createDirectories(notesDirectory);
        }

        if (!Files.exists(categoriesDirectory)) {
            Files.createDirectories(categoriesDirectory);
        }

        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void saveNote(Note note) throws IOException {
        String fileName = notesDir + "/" + note.getId() + ".json";

        ObjectNode noteJson = objectMapper.createObjectNode();
        noteJson.put("id", note.getId());
        noteJson.put("title", note.getTitle());
        noteJson.put("type", note.getType());
        noteJson.put("createdAt", formatDateTime(note.getCreatedAt()));
        noteJson.put("updatedAt", formatDateTime(note.getUpdatedAt()));

        // Add type-specific fields
        if (note instanceof TextNote) {
            TextNote textNote = (TextNote) note;
            noteJson.put("text", textNote.getText());
        } else if (note instanceof URLNote) {
            URLNote urlNote = (URLNote) note;
            noteJson.put("url", urlNote.getUrl());
            noteJson.put("description", urlNote.getDescription());
        } else if (note instanceof ImageNote) {
            ImageNote imageNote = (ImageNote) note;
            noteJson.put("imagePath", imageNote.getImagePath());
            noteJson.put("description", imageNote.getDescription());
        }

        objectMapper.writeValue(new File(fileName), noteJson);
    }

    @Override
    public void deleteNote(String noteId) throws IOException {
        String fileName = notesDir + "/" + noteId + ".json";
        File file = new File(fileName);

        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Failed to delete note file: " + fileName);
            }
        }
    }

    @Override
    public List<Note> loadAllNotes() throws IOException {
        List<Note> notes = new ArrayList<>();
        File notesFolder = new File(notesDir);

        if (!notesFolder.exists() || !notesFolder.isDirectory()) {
            return notes;
        }

        File[] noteFiles = notesFolder.listFiles((dir, name) -> name.endsWith(".json"));
        if (noteFiles == null) {
            return notes;
        }

        for (File file : noteFiles) {
            try {
                Map<String, Object> noteData = objectMapper.readValue(
                        file, new TypeReference<Map<String, Object>>() {});

                String id = (String) noteData.get("id");
                String title = (String) noteData.get("title");
                String type = (String) noteData.get("type");
                LocalDateTime createdAt = parseDateTime((String) noteData.get("createdAt"));
                LocalDateTime updatedAt = parseDateTime((String) noteData.get("updatedAt"));

                Note note = null;
                if ("TEXT".equals(type)) {
                    String text = (String) noteData.get("text");
                    note = new TextNote(id, title, text, createdAt, updatedAt);
                } else if ("URL".equals(type)) {
                    String url = (String) noteData.get("url");
                    String description = (String) noteData.get("description");
                    note = new URLNote(id, title, url, description, createdAt, updatedAt);
                } else if ("IMAGE".equals(type)) {
                    String imagePath = (String) noteData.get("imagePath");
                    String description = (String) noteData.get("description");
                    note = new ImageNote(id, title, imagePath, description, createdAt, updatedAt);
                }

                if (note != null) {
                    notes.add(note);
                }
            } catch (Exception e) {
                System.err.println("Error loading note from " + file.getPath() + ": " + e.getMessage());
            }
        }

        return notes;
    }

    @Override
    public void saveCategory(Category category) throws IOException {
        String fileName = categoriesDir + "/" + category.getId() + ".json";

        ObjectNode categoryJson = objectMapper.createObjectNode();
        categoryJson.put("id", category.getId());
        categoryJson.put("name", category.getName());
        categoryJson.set("noteIds", objectMapper.valueToTree(category.getNoteIds()));

        objectMapper.writeValue(new File(fileName), categoryJson);
    }

    @Override
    public void deleteCategory(String categoryId) throws IOException {
        String fileName = categoriesDir + "/" + categoryId + ".json";
        File file = new File(fileName);

        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Failed to delete category file: " + fileName);
            }
        }
    }

    @Override
    public List<Category> loadAllCategories() throws IOException {
        List<Category> categories = new ArrayList<>();
        File categoriesFolder = new File(categoriesDir);

        if (!categoriesFolder.exists() || !categoriesFolder.isDirectory()) {
            return categories;
        }

        File[] categoryFiles = categoriesFolder.listFiles((dir, name) -> name.endsWith(".json"));
        if (categoryFiles == null) {
            return categories;
        }

        for (File file : categoryFiles) {
            try {
                Map<String, Object> categoryData = objectMapper.readValue(
                        file, new TypeReference<Map<String, Object>>() {});

                String id = (String) categoryData.get("id");
                String name = (String) categoryData.get("name");
                List<String> noteIds = objectMapper.convertValue(
                        categoryData.get("noteIds"), new TypeReference<List<String>>() {});

                categories.add(new Category(id, name, noteIds));
            } catch (Exception e) {
                System.err.println("Error loading category from " + file.getPath() + ": " + e.getMessage());
            }
        }

        return categories;
    }

    /**
     * Formats a LocalDateTime object as a string.
     *
     * @param dateTime The LocalDateTime to format
     * @return The formatted string
     */
    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * Parses a string into a LocalDateTime object.
     *
     * @param dateTimeStr The string to parse
     * @return The parsed LocalDateTime
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
    }
}