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

@WebServlet("/viewNote.html")
public class ViewNoteServlet extends HttpServlet {

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

        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/viewNote.jsp");
        dispatch.forward(request, response);
    }
}