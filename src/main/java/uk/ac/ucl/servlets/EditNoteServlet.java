package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/editNote.html")
public class EditNoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String noteId = request.getParameter("id");

        if (noteId == null || noteId.isEmpty()) {
            response.sendRedirect("notes.html");
            return;
        }

        Model model = ModelFactory.getModel();
        Note note = model.getNote(noteId);

        if (note == null) {
            response.sendRedirect("notes.html");
            return;
        }

        request.setAttribute("note", note);
        request.setAttribute("categories", model.getAllCategories());

        // Get categories this note belongs to
        List<Category> noteCategories = model.getAllCategories().stream()
                .filter(category -> category.containsNote(noteId))
                .toList();
        request.setAttribute("noteCategories", noteCategories);

        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/editNote.jsp");
        dispatch.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String noteId = request.getParameter("id");
        String title = request.getParameter("title");

        if (noteId == null || noteId.isEmpty() || title == null || title.trim().isEmpty()) {
            response.sendRedirect("notes.html");
            return;
        }

        Model model = ModelFactory.getModel();
        Note note = model.getNote(noteId);

        if (note == null) {
            response.sendRedirect("notes.html");
            return;
        }

        // Update the note based on its type
        if (note instanceof TextNote) {
            String text = request.getParameter("text");
            model.updateTextNote(noteId, title, text);
        } else if (note instanceof URLNote) {
            String url = request.getParameter("url");
            String description = request.getParameter("description");

            if (url == null || url.trim().isEmpty()) {
                request.setAttribute("error", "URL cannot be empty");
                doGet(request, response);
                return;
            }

            model.updateURLNote(noteId, title, url, description);
        }

        // Update category assignments
        // First remove from all categories
        for (Category category : model.getAllCategories()) {
            if (category.containsNote(noteId)) {
                model.removeNoteFromCategory(noteId, category.getId());
            }
        }

        // Then add to selected categories
        String[] categoryIds = request.getParameterValues("categories");
        if (categoryIds != null) {
            for (String categoryId : categoryIds) {
                model.addNoteToCategory(noteId, categoryId);
            }
        }

        response.sendRedirect("viewNote.html?id=" + noteId);
    }
}