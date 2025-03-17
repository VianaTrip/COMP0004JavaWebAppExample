<%@ page import="uk.ac.ucl.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="/meta.jsp"/>
    <title>Manage Categories</title>
    <style>
        .main-content {
            max-width: 800px;
            margin: 0 auto;
        }
        .form-group {
            margin-bottom: 15px;
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
            margin-right: 5px;
        }
        .button-secondary {
            background-color: #6c757d;
        }
        .button-delete {
            background-color: #dc3545;
        }
        .category-list {
            list-style-type: none;
            padding: 0;
        }
        .category-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .category-info {
            flex-grow: 1;
        }
        .category-actions {
            display: flex;
        }
    </style>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
    <h1>Manage Categories</h1>

    <div class="main-content">
        <!-- Create new category form -->
        <h2>Create New Category</h2>
        <form method="post" action="manageCategories.html">
            <input type="hidden" name="action" value="create">
            <div class="form-group">
                <label for="categoryName">Category Name:</label>
                <input type="text" id="categoryName" name="categoryName" required>
            </div>
            <div class="form-group">
                <button type="submit" class="button">Create Category</button>
                <a href="notes.html" class="button button-secondary">Back to Notes</a>
            </div>
        </form>

        <hr>

        <!-- List and manage existing categories -->
        <h2>Existing Categories</h2>
        <%
            List<Category> categories = (List<Category>) request.getAttribute("categories");
            if (categories != null && !categories.isEmpty()) {
        %>
            <ul class="category-list">
                <% for (Category category : categories) { %>
                    <li class="category-item">
                        <div class="category-info">
                            <h3><%= category.getName() %></h3>
                            <p>Contains <%= category.getNoteCount() %> notes</p>
                        </div>
                        <div class="category-actions">
                            <button type="button" class="button"
                                    onclick="showEditForm('<%= category.getId() %>', '<%= category.getName() %>')">
                                Edit
                            </button>
                            <form method="post" action="manageCategories.html"
                                  onsubmit="return confirm('Are you sure you want to delete this category? Notes in this category will not be deleted.');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="categoryId" value="<%= category.getId() %>">
                                <button type="submit" class="button button-delete">Delete</button>
                            </form>
                        </div>
                    </li>
                <% } %>
            </ul>

            <!-- Hidden edit form that will be shown when clicking Edit -->
            <div id="editFormContainer" style="display: none; margin-top: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 4px;">
                <h3>Edit Category</h3>
                <form method="post" action="manageCategories.html">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" id="editCategoryId" name="categoryId" value="">
                    <div class="form-group">
                        <label for="editCategoryName">Category Name:</label>
                        <input type="text" id="editCategoryName" name="categoryName" required>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="button">Save Changes</button>
                        <button type="button" class="button button-secondary"
                                onclick="document.getElementById('editFormContainer').style.display='none'">
                            Cancel
                        </button>
                    </div>
                </form>
            </div>

            <script>
                function showEditForm(categoryId, categoryName) {
                    document.getElementById('editCategoryId').value = categoryId;
                    document.getElementById('editCategoryName').value = categoryName;
                    document.getElementById('editFormContainer').style.display = 'block';
                }
            </script>
        <% } else { %>
            <p>No categories have been created yet.</p>
        <% } %>
    </div>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>