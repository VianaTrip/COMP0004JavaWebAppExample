package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Category;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;

@WebServlet("/manageCategories.html")
public class ManageCategoriesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Model model = ModelFactory.getModel();
        request.setAttribute("categories", model.getAllCategories());

        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/manageCategories.jsp");
        dispatch.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        Model model = ModelFactory.getModel();

        if ("create".equals(action)) {
            String categoryName = request.getParameter("categoryName");
            if (categoryName != null && !categoryName.trim().isEmpty()) {
                model.createCategory(categoryName.trim());
            }
        } else if ("edit".equals(action)) {
            String categoryId = request.getParameter("categoryId");
            String categoryName = request.getParameter("categoryName");
            if (categoryId != null && categoryName != null && !categoryName.trim().isEmpty()) {
                model.updateCategory(categoryId, categoryName.trim());
            }
        } else if ("delete".equals(action)) {
            String categoryId = request.getParameter("categoryId");
            if (categoryId != null) {
                model.deleteCategory(categoryId);
            }
        }

        response.sendRedirect("manageCategories.html");
    }
}