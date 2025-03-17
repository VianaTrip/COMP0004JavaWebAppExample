package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/getImage.html")
public class GetImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String imagePath = request.getParameter("path");

        if (imagePath == null || imagePath.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Validate the path to prevent directory traversal attacks
        Path path = Paths.get(imagePath);
        if (!path.normalize().toString().equals(path.toString())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        File file = new File(imagePath);
        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Set the content type based on file extension
        String contentType = getServletContext().getMimeType(file.getName());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        response.setContentType(contentType);

        // Set content length
        response.setContentLength((int) file.length());

        // Stream the file to the client
        Files.copy(file.toPath(), response.getOutputStream());
    }
}