<%@ page import="uk.ac.ucl.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="/meta.jsp"/>
    <title>Manage Categories</title>
    <style>
        .container {
            max-width: 800px;
            margin: 0 auto;
        }
        .category-list {
            margin: 20px 0;
        }
        .category-item {
            display: flex;
            align-items: center;
            padding: 10px;
            margin-bottom: 10px;
            background-color: #f8f9fa;
            border-radius: 4px;
        }
        .category-name {
            flex-grow: 1;
            font-weight: bold;
        }
        .category-count {
            margin-right: 15px;
            color: #6c757d;
        }
        .category-actions {
            display: flex;
            gap: 5px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .new-category-form {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="text"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
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
        }
        .button-small {
            padding: 5px 10px;
            font-size: 0.9em;
        }
        .button-secondary {
            background-color: #6c757d;
        }
        .button-danger {
            background-color: #dc3545;
        }
        .edit-form {
            display: none;
            margin-top: 10px;
        }
        .edit-active .edit-form {
            display: flex;
            align-items: center;
            gap: 5px;
        }
        .edit-active .category-name,
        .edit-active .category-count,
        .edit-active .category-actions {
            display: none;
        }
    </style>
    <script type="text/javascript">
        function toggleEditForm(categoryId) {
            const categoryItem = document.getElementById('category-' + categoryId);
            categoryItem.classList.toggle('edit-active');
        }
    </script>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
    <div class="container">
        <h1>Manage Categories</h1>

        <div class="new-category-form">
            <h2>Create New Category</h2>
            <form method="post" action="manageCategories.html">
                <input type="hidden" name="action" value="create">
                <div class="form-group">
                    <label for="categoryName">Category Name:</label>
                    <input type="text" id="categoryName" name="categoryName" required>
                </div>
                <div class="form-group">
                    <button type="submit" class="button">Create Category</button>
                </div>
            </form>
        </div>

        <h2>Existing Categories</h2>
        <div class="category-list">
            <%
                List<Category> categories = (List<Category>) request.getAttribute("categories");
                if (categories != null && !categories.isEmpty()) {
                    for (Category category : categories) {
            %>
                <div id="category-<%= category.getId() %>" class="category-item">
                    <div class="category-name"><%= category.getName() %></div>
                    <div class="category-count"><%= category.getNoteCount() %> notes</div>
                    <div class="category-actions">
                        <button onclick="toggleEditForm('<%= category.getId() %>')" class="button button-small button-secondary">Edit</button>
                        <form method="post" action="manageCategories.html" onsubmit="return confirm('Are you sure you want to delete this category?')">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="categoryId" value="<%= category.getId() %>">
                            <button type="submit" class="button button-small button-danger">Delete</button>
                        </form>
                    </div>
                    <div class="edit-form">
                        <form method="post" action="manageCategories.html">
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="categoryId" value="<%= category.getId() %>">
                            <input type="text" name="categoryName" value="<%= category.getName() %>" required>
                            <button type="submit" class="button button-small">Save</button>
                            <button type="button" onclick="toggleEditForm('<%= category.getId() %>')" class="button button-small button-secondary">Cancel</button>
                        </form>
                    </div>
                </div>
            <%
                    }
                } else {
            %>
                <p>No categories created yet.</p>
            <%
                }
            %>
        </div>

        <div class="form-group">
            <a href="notes.html" class="button button-secondary">Back to Notes</a>
        </div>
    </div>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>