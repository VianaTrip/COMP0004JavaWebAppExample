```jsp
<%@ page import="java.util.List" %>
<%@ page import="uk.ac.ucl.model.Note" %>
<%@ page import="uk.ac.ucl.model.Category" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="/meta.jsp"/>
    <title>Notes Application</title>
    <style>
        .note-list {
            list-style-type: none;
            padding: 0;
        }
        .note-list li {
            margin-bottom: 15px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .note-type {
            color: #666;
            font-size: 0.8em;
            margin: 0;
        }
        .note-preview {
            margin-top: 5px;
            color: #333;
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
            border-radius: 4px;
            margin-right: 10px;
        }
        .sidebar {
            float: right;
            width: 25%;
            padding: 15px;
            margin-left: 20px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        .category-list {
            list-style-type: none;
            padding: 0;
        }
        .category-list li {
            margin-bottom: 5px;
        }
        .main {
            margin-right: 30%;
        }
    </style>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
    <%
        List<Note> notes = (List<Note>) request.getAttribute("notes");
        String categoryName = (String) request.getAttribute("categoryName");

        if (categoryName != null) {
    %>
        <h1>Notes in Category: <%= categoryName %></h1>
    <% } else { %>
        <h1>All Notes</h1>
    <% } %>

    <div class="actions">
        <a href="createNote.html" class="button">Create New Note</a>
        <a href="search.html" class="button">Search Notes</a>
    </div>

    <% if (notes != null && !notes.isEmpty()) { %>
        <ul class="note-list">
            <% for (Note note : notes) { %>
                <li>
                    <a href="viewNote.html?id=<%= note.getId() %>">
                        <h3><%= note.getTitle() %></h3>
                        <p class="note-type"><%= note.getType() %></p>
                        <p class="note-preview"><%= note.getContent().length() > 100 ?
                            note.getContent().substring(0, 100) + "..." :
                            note.getContent() %></p>
                    </a>
                </li>
            <% } %>
        </ul>
    <% } else { %>
        <p>No notes found.</p>
    <% } %>
</div>

<div class="sidebar">
    <h2>Categories</h2>
    <ul class="category-list">
        <li><a href="notes.html">All Notes</a></li>
        <%
            List<Category> categories = (List<Category>) request.getAttribute("categories");
            if (categories != null) {
                for (Category category : categories) {
        %>
            <li>
                <a href="notes.html?categoryId=<%= category.getId() %>"><%= category.getName() %></a>
            </li>
        <%
                }
            }
        %>
    </ul>
    <div class="actions">
        <a href="manageCategories.html" class="button">Manage Categories</a>
    </div>
</div>

<jsp:include page="/footer.jsp"/>
</body>
</html>
```