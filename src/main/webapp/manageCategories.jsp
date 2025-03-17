<%@ page import="uk.ac.ucl.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="/meta.jsp"/>
    <title>Manage Categories</title>
    <style>
        .category-list {
            list-style-type: none;
            padding: 0;
        }
        .category-list li {
            margin-bottom: 10px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .category-name {
            font-weight: bold;
        }
        .category-count {
            color: #666;
            font-size: 0.9em;
        }
        .actions {
            margin: 20px 0;
        }
        .button {
            display: inline-block;
            padding: 8px 15px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-right: 10px;
        }
        .button-delete {
            background-color: #dc3545;
        }
        .create-form {
            margin: 20px 0;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        input[type="text"] {
            padding: 8px;
            width: 300px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
    <h1>Manage Categories</h1>

    <div class="create-form">
        <h2>Create New Category</h2>
        <form method="post" action="createCategory.html">
            <div class="form-group">
                <label for="categoryName">Category Name:</label>
                <input type="text" id="categoryName" name="categoryName" required>
                <button type="submit" class="button">Create</button>
            </div>
        </form>
    </div>

    <h2>Categories</h2>
    <%
        List<Category> categories = (List<Category>) request.getAttribute("categories");
        if (categories != null && !categories.isEmpty()) {
    %>
        <ul class="category-list">
            <% for (Category category : categories) { %>
                <li>
                    <div>
                        <span class="category-name"><%= category.getName() %></span>
                        <span class="category-count">(<%= category.getNoteCount() %> notes)</span>
                    </div>
                    <div class="actions">
                        <a href="notes.html?categoryId=<%= category.getId() %>" class="button">View Notes</a>
                        <a href="deleteCategory.html?id=<%= category.getId() %>"
                           class="button button-delete"
                           onclick="return confirm('Are you sure you want to delete this category? Notes will not be deleted.')">
                            Delete
                        </a>
                    </div>
                </li>
            <% } %>
        </ul>
    <% } else { %>
        <p>No categories exist yet. Create one using the form above.</p>
    <% } %>

    <div class="actions">
        <a href="notes.html" class="button">Back to Notes</a>
    </div>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>