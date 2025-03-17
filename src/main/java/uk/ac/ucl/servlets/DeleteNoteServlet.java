package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;

@WebServlet("/deleteNote.html")
public class DeleteNoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String noteId = request.getParameter("id");

        if (noteId == null || noteId.isEmpty()) {
            response.sendRedirect("notes.html");
            return;
        }

        Model model = ModelFactory.getModel();
        boolean deleted = model.deleteNote(noteId);

        // Redirect to notes list regardless of whether deletion was successful
        response.sendRedirect("notes.html");
    }
}