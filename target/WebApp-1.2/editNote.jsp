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
        .button-danger {
            background-color: #dc3545;
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
        .error {
            color: #dc3545;
            margin-bottom: 15px;
            padding: 10px;
            background-color: #f8d7da;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
    <%
        Note note = (Note) request.getAttribute("note");
        if (note == null) {
    %>
        <p>Note not found. <a href="notes.html">Return to notes</a></p>
    <% } else { %>
        <h1>Edit Note</h1>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <div class="form-container">
            <form method="post" action="editNote.html">
                <input type="hidden" name="id" value="<%= note.getId() %>">

                <div class="form-group">
                    <label for="title">Title:</label>
                    <input type="text" id="title" name="title" value="<%= note.getTitle() %>" required>
                </div>

                <% if (note instanceof URLNote) {
                    URLNote urlNote = (URLNote) note;
                %>
                    <input type="hidden" name="type" value="URL">
                    <div class="form-group">
                        <label for="url">URL:</label>
                        <input type="text" id="url" name="url" value="<%= urlNote.getUrl() %>" required>
                    </div>

                    <div class="form-group">
                        <label for="description">Description (optional):</label>
                        <textarea id="description" name="description"><%= urlNote.getDescription() %></textarea>
                    </div>
                <% } else if (note instanceof TextNote) {
                    TextNote textNote = (TextNote) note;
                %>
                    <input type="hidden" name="type" value="TEXT">
                    <div class="form-group">
                        <label for="text">Content:</label>
                        <textarea id="text" name="text"><%= textNote.getText() %></textarea>
                    </div>
                <% } %>

                <div class="form-group">
                    <label>Categories:</label>
                    <div class="category-list">
                        <%
                            List<Category> categories = (List<Category>) request.getAttribute("categories");
                            if (categories != null && !categories.isEmpty()) {
                                for (Category category : categories) {
                                    boolean inCategory = category.containsNote(note.getId());
                        %>
                            <div class="category-item">
                                <input type="checkbox" id="category-<%= category.getId() %>"
                                       name="categories" value="<%= category.getId() %>"
                                       <%= inCategory ? "checked" : "" %>>
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
                    <button type="submit" class="button">Save Changes</button>
                    <a href="viewNote.html?id=<%= note.getId() %>" class="button button-secondary">Cancel</a>
                    <a href="deleteNote.html?id=<%= note.getId() %>" class="button button-danger"
                       onclick="return confirm('Are you sure you want to delete this note?')">Delete</a>
                </div>
            </form>
        </div>
    <% } %>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>