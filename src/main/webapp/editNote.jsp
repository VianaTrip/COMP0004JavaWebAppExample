<%@ page import="uk.ac.ucl.model.Note" %>
<%@ page import="uk.ac.ucl.model.TextNote" %>
<%@ page import="uk.ac.ucl.model.URLNote" %>
<%@ page import="uk.ac.ucl.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="/meta.jsp"/>
    <title>Edit Note</title>
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
            resize: vertical;
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
        .button-cancel {
            background-color: #dc3545;
        }
        .category-options {
            max-height: 150px;
            overflow-y: auto;
            border: 1px solid #ddd;
            padding: 10px;
            border-radius: 4px;
        }
        .category-option {
            margin-bottom: 5px;
        }
    </style>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
    <%
        Note note = (Note) request.getAttribute("note");
        List<Category> allCategories = (List<Category>) request.getAttribute("categories");
        List<Category> noteCategories = (List<Category>) request.getAttribute("noteCategories");

        if (note != null) {
    %>
        <h1>Edit Note</h1>

        <div class="form-container">
            <form method="post" action="editNote.html">
                <input type="hidden" name="id" value="<%= note.getId() %>">
                <input type="hidden" name="type" value="<%= note.getType() %>">

                <div class="form-group">
                    <label for="title">Title:</label>
                    <input type="text" id="title" name="title" value="<%= note.getTitle() %>" required>
                </div>

                <% if ("TEXT".equals(note.getType())) {
                    TextNote textNote = (TextNote) note;
                %>
                    <div class="form-group">
                        <label for="text">Content:</label>
                        <textarea id="text" name="text"><%= textNote.getText() %></textarea>
                    </div>
                <% } else if ("URL".equals(note.getType())) {
                    URLNote urlNote = (URLNote) note;
                %>
                    <div class="form-group">
                        <label for="url">URL:</label>
                        <input type="text" id="url" name="url" value="<%= urlNote.getUrl() %>" required>
                    </div>

                    <div class="form-group">
                        <label for="description">Description:</label>
                        <textarea id="description" name="description"><%= urlNote.getDescription() %></textarea>
                    </div>
                <% } %>

                <div class="form-group">
                    <label>Categories:</label>
                    <div class="category-options">
                        <% if (allCategories != null && !allCategories.isEmpty()) {
                            for (Category category : allCategories) {
                                boolean isChecked = false;
                                if (noteCategories != null) {
                                    for (Category noteCategory : noteCategories) {
                                        if (noteCategory.getId().equals(category.getId())) {
                                            isChecked = true;
                                            break;
                                        }
                                    }
                                }
                        %>
                            <div class="category-option">
                                <input type="checkbox" id="category-<%= category.getId() %>"
                                       name="categories" value="<%= category.getId() %>"
                                       <%= isChecked ? "checked" : "" %>>
                                <label for="category-<%= category.getId() %>"><%= category.getName() %></label>
                            </div>
                        <% } } else { %>
                            <p>No categories available.</p>
                        <% } %>
                    </div>
                </div>

                <div class="form-group">
                    <button type="submit" class="button">Save Changes</button>
                    <a href="viewNote.html?id=<%= note.getId() %>" class="button button-secondary">Cancel</a>
                </div>
            </form>
        </div>
    <% } else { %>
        <p>Note not found.</p>
        <a href="notes.html" class="button">Back to Notes</a>
    <% } %>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>