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
import uk.ac.ucl.model.Category;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/searchNotes.html")
public class SearchNotesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Model model = ModelFactory.getModel();
        request.setAttribute("categories", model.getAllCategories());

        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/search.jsp");
        dispatch.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("query");
        String[] noteTypes = request.getParameterValues("noteTypes");
        String categoryFilter = request.getParameter("categoryFilter");
        String sortBy = request.getParameter("sortBy");

        if (query == null || query.trim().isEmpty()) {
            response.sendRedirect("notes.html");
            return;
        }

        Model model = ModelFactory.getModel();
        List<Note> results = model.searchNotes(query);

        // Apply note type filter if specified
        if (noteTypes != null && noteTypes.length > 0) {
            // Convert to list for contains check
            List<String> types = List.of(noteTypes);
            results = results.stream()
                    .filter(note -> types.contains(note.getType()))
                    .collect(Collectors.toList());
        }

        // Apply category filter if specified
        if (categoryFilter != null && !categoryFilter.isEmpty()) {
            List<Note> categoryNotes = model.getNotesInCategory(categoryFilter);
            results = results.stream()
                    .filter(categoryNotes::contains)
                    .collect(Collectors.toList());
        }

        // Apply sorting if specified
        if (sortBy != null) {
            switch (sortBy) {
                case "title":
                    results.sort(Comparator.comparing(Note::getTitle));
                    break;
                case "newest":
                    results.sort(Comparator.comparing(Note::getCreatedAt).reversed());
                    break;
                case "oldest":
                    results.sort(Comparator.comparing(Note::getCreatedAt));
                    break;
                // For "relevance", we don't change the order as we assume
                // the search results are already ordered by relevance
                default:
                    break;
            }
        }

        request.setAttribute("query", query);
        request.setAttribute("results", results);
        request.setAttribute("categories", model.getAllCategories());
        request.setAttribute("selectedCategory", categoryFilter);
        request.setAttribute("selectedNoteTypes", noteTypes);
        request.setAttribute("selectedSort", sortBy);

        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/searchResults.jsp");
        dispatch.forward(request, response);
    }
}