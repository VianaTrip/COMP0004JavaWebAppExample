package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.Note;

import java.io.IOException;
import java.util.List;

@WebServlet("/notes.html")
public class ViewNotesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the model
        Model model = ModelFactory.getModel();

        // Get all notes or notes from a specific category if categoryId is provided
        String categoryId = request.getParameter("categoryId");
        List<Note> notes;

        if (categoryId != null && !categoryId.isEmpty()) {
            notes = model.getNotesInCategory(categoryId);
            request.setAttribute("categoryName", model.getCategory(categoryId).getName());
        } else {
            notes = model.getAllNotes();
        }

        // Add the notes to the request
        request.setAttribute("notes", notes);

        // Also add categories for navigation
        request.setAttribute("categories", model.getAllCategories());

        // Forward to the JSP page
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/noteList.jsp");
        dispatch.forward(request, response);
    }
}