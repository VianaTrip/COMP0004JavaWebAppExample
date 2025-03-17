package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@WebServlet("/uploadImage.html")
@MultipartConfig
public class UploadImageServlet extends HttpServlet {

    private static final String IMAGES_DIR = "data/images";

    @Override
    public void init() throws ServletException {
        // Create the images directory if it doesn't exist
        try {
            Path imagesPath = Paths.get(IMAGES_DIR);
            if (!Files.exists(imagesPath)) {
                Files.createDirectories(imagesPath);
            }
        } catch (IOException e) {
            throw new ServletException("Failed to create images directory", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String caption = request.getParameter("imageCaption");
        Part filePart = request.getPart("imageFile");

        // Validate input
        if (title == null || title.trim().isEmpty() || filePart == null) {
            response.sendRedirect("createNote.html?type=IMAGE&error=missingData");
            return;
        }

        // Generate a unique filename to prevent collisions
        String fileName = UUID.randomUUID().toString() + getFileExtension(filePart);
        String filePath = IMAGES_DIR + File.separator + fileName;

        // Save the file
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }

        // Create the ImageNote
        Model model = ModelFactory.getModel();
        String noteId = model.createImageNote(title, filePath, caption).getId();

        // Add to categories if specified
        String[] categoryIds = request.getParameterValues("categories");
        if (categoryIds != null) {
            for (String categoryId : categoryIds) {
                model.addNoteToCategory(noteId, categoryId);
            }
        }

        response.sendRedirect("viewNote.html?id=" + noteId);
    }

    private String getFileExtension(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");

        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                String filename = element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
                int lastDot = filename.lastIndexOf('.');
                if (lastDot > 0) {
                    return filename.substring(lastDot);
                }
            }
        }

        // Default extension for images
        return ".jpg";
    }
}