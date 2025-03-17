<%@ page import="uk.ac.ucl.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="/meta.jsp"/>
    <title>Create New Note</title>
    <style>
        .form-container {
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
        input[type="text"], textarea, select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        textarea {
            min-height: 200px;
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
        .button-secondary {
            background-color: #6c757d;
        }
        .category-list {
            max-height: 150px;
            overflow-y: auto;
            border: 1px solid #ddd;
            padding: 10px;
            border-radius: 4px;
        }
        .category-item {
            margin-bottom: 5px;
        }
    </style>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
    <h1>Create New Note</h1>

    <div class="form-container">
        <%
            String noteType = (String) request.getAttribute("noteType");
            if (noteType == null) {
                noteType = "TEXT";
            }
        %>

        <form method="post" action="createNote.html">
            <input type="hidden" name="type" value="<%= noteType %>">

            <div class="form-group">
                <label for="title">Title:</label>
                <input type="text" id="title" name="title" required>
            </div>

            <% if ("URL".equals(noteType)) { %>
                <div class="form-group">
                    <label for="url">URL:</label>
                    <input type="text" id="url" name="url" required>
                </div>

                <div class="form-group">
                    <label for="description">Description (optional):</label>
                    <textarea id="description" name="description"></textarea>
                </div>
            <% } else { %>
                <div class="form-group">
                    <label for="text">Content:</label>
                    <textarea id="text" name="text"></textarea>
                </div>
            <% } %>

            <div class="form-group">
                <label>Categories (optional):</label>
                <div class="category-list">
                    <%
                        List<Category> categories = (List<Category>) request.getAttribute("categories");
                        if (categories != null && !categories.isEmpty()) {
                            for (Category category : categories) {
                    %>
                        <div class="category-item">
                            <input type="checkbox" id="category-<%= category.getId() %>"
                                   name="categories" value="<%= category.getId() %>">
                            <label for="category-<%= category.getId() %>"><%= category.getName() %></label>
                        </div>
                    <%
                            }
                        } else {
                    %>
                        <p>No categories available. <a href="manageCategories.html">Create categories</a> first.</p>
                    <%
                        }
                    %>
                </div>
            </div>

            <div class="form-group">
                <button type="submit" class="button">Create Note</button>
                <a href="notes.html" class="button button-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>