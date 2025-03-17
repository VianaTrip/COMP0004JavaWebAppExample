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
        request.setAttribute("noteCategories", model.getCategoriesForNote(noteId));

        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/editNote.jsp");
        dispatch.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String noteId = request.getParameter("id");
        String title = request.getParameter("title");
        String type = request.getParameter("type");

        if (noteId == null || noteId.isEmpty() || title == null || title.isEmpty()) {
            response.sendRedirect("notes.html");
            return;
        }

        Model model = ModelFactory.getModel();
        Note note = model.getNote(noteId);

        if (note == null) {
            response.sendRedirect("notes.html");
            return;
        }

        boolean updated = false;

        if ("URL".equals(type)) {
            String url = request.getParameter("url");
            String description = request.getParameter("description");

            if (url != null && !url.isEmpty()) {
                updated = model.updateURLNote(noteId, title, url, description);
            }
        } else if ("TEXT".equals(type)) {
            String text = request.getParameter("text");
            updated = model.updateTextNote(noteId, title, text);
        } else if ("IMAGE".equals(type)) {
            // Handle image note update if you implement ImageNote class
            // Currently not implemented in the base model
        }

        if (updated) {
            // Update category assignments
            // First, remove note from all categories
            List<Category> allCategories = model.getAllCategories();
            for (Category category : allCategories) {
                model.removeNoteFromCategory(noteId, category.getId());
            }

            // Then, add to selected categories
            String[] categoryIds = request.getParameterValues("categories");
            if (categoryIds != null) {
                for (String categoryId : categoryIds) {
                    model.addNoteToCategory(noteId, categoryId);
                }
            }
        }

        response.sendRedirect("viewNote.html?id=" + noteId);
    }
}