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
        // Get the ID of the note to delete
        String noteId = request.getParameter("id");

        if (noteId != null && !noteId.isEmpty()) {
            // Delete the note using the model
            Model model = ModelFactory.getModel();
            model.deleteNote(noteId);
        }

        // Redirect back to the notes list
        response.sendRedirect("notes.html");
    }
}