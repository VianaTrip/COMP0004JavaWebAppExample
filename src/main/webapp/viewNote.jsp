```jsp
<%@ page import="uk.ac.ucl.model.Note" %>
<%@ page import="uk.ac.ucl.model.TextNote" %>
<%@ page import="uk.ac.ucl.model.URLNote" %>
<%@ page import="uk.ac.ucl.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="/meta.jsp"/>
    <title>View Note</title>
    <style>
        .note-metadata {
            background-color: #f8f9fa;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .note-metadata p {
            margin: 5px 0;
        }
        .note-content {
            margin-bottom: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .url-note a {
            color: #007bff;
            text-decoration: none;
            word-break: break-all;
        }
        .url-note .description {
            margin-top: 10px;
            padding-top: 10px;
            border-top: 1px solid #eee;
        }
        .actions {
            margin-top: 20px;
        }
        .button {
            display: inline-block;
            padding: 8px 15px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin-right: 10px;
        }
        .button.delete {
            background-color: #dc3545;
        }
        .category-tag {
            display: inline-block;
            background-color: #6c757d;
            color: white;
            padding: 3px 8px;
            border-radius: 3px;
            margin-right: 5px;
            text-decoration: none;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
    <%
        Note note = (Note) request.getAttribute("note");
        List<Category> categories = (List<Category>) request.getAttribute("categories");

        if (note != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    %>
        <h1><%= note.getTitle() %></h1>

        <div class="note-metadata">
            <p>Created: <%= note.getCreatedAt().format(formatter) %></p>
            <p>Updated: <%= note.getUpdatedAt().format(formatter) %></p>
            <p>Type: <%= note.getType() %></p>

            <% if (categories != null && !categories.isEmpty()) { %>
                <p>Categories:
                <%
                    for (Category category : categories) {
                        if (category.containsNote(note.getId())) {
                %>
                    <a href="notes.html?categoryId=<%= category.getId() %>" class="category-tag"><%= category.getName() %></a>
                <%
                        }
                    }
                %>
                </p>
            <% } %>
        </div>

        <div class="note-content">
            <% if (note instanceof TextNote) { %>
                <div class="text-note">
                    <%= ((TextNote) note).getText().replace("\n", "<br>") %>
                </div>
            <% } else if (note instanceof URLNote) { %>
                <div class="url-note">
                    <a href="<%= ((URLNote) note).getUrl() %>" target="_blank"><%= ((URLNote) note).getUrl() %></a>
                    <% if (!((URLNote) note).getDescription().isEmpty()) { %>
                        <div class="description">
                            <%= ((URLNote) note).getDescription().replace("\n", "<br>") %>
                        </div>
                    <% } %>
                </div>
            <% } %>
        </div>

        <div class="actions">
            <a href="editNote.html?id=<%= note.getId() %>" class="button">Edit</a>
            <a href="deleteNote.html?id=<%= note.getId() %>" class="button delete" onclick="return confirm('Are you sure you want to delete this note?')">Delete</a>
            <a href="notes.html" class="button">Back to Notes</a>
        </div>
    <% } else { %>
        <p>Note not found.</p>
        <a href="notes.html" class="button">Back to Notes</a>
    <% } %>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
```