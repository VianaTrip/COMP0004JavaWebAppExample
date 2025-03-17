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

import java.io.IOException;

@WebServlet("/createNote.html")
public class CreateNoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String noteType = request.getParameter("type");
        if (noteType == null) {
            noteType = "TEXT"; // Default type
        }

        Model model = ModelFactory.getModel();
        request.setAttribute("categories", model.getAllCategories());
        request.setAttribute("noteType", noteType);

        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/createNote.jsp");
        dispatch.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String type = request.getParameter("type");

        if (title == null || title.trim().isEmpty()) {
            response.sendRedirect("createNote.html");
            return;
        }

        Model model = ModelFactory.getModel();
        String noteId;

        if ("URL".equals(type)) {
            String url = request.getParameter("url");
            String description = request.getParameter("description");

            if (url == null || url.trim().isEmpty()) {
                response.sendRedirect("createNote.html?type=URL");
                return;
            }

            noteId = model.createURLNote(title, url, description).getId();
        } else {
            // Default to TEXT type
            String text = request.getParameter("text");
            noteId = model.createTextNote(title, text).getId();
        }

        // Add to categories if specified
        String[] categoryIds = request.getParameterValues("categories");
        if (categoryIds != null) {
            for (String categoryId : categoryIds) {
                model.addNoteToCategory(noteId, categoryId);
            }
        }

        response.sendRedirect("viewNote.html?id=" + noteId);
    }
}