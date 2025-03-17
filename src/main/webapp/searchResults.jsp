<%@ page import="uk.ac.ucl.model.Note" %>
<%@ page import="uk.ac.ucl.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="/meta.jsp"/>
    <title>Search Results</title>
    <style>
        .search-info {
            margin-bottom: 20px;
            padding: 10px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
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
        .button {
            display: inline-block;
            padding: 8px 15px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin-right: 10px;
        }
        .highlight {
            background-color: #ffc;
            padding: 0 2px;
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
    <h1>Search Results</h1>

    <div class="search-info">
        <%
            String query = (String) request.getAttribute("query");
            List<Note> results = (List<Note>) request.getAttribute("results");
        %>
        <p>Search results for: <strong><%= query %></strong></p>
        <p>Found <%= results.size() %> matching notes.</p>
        <a href="searchNotes.html" class="button">New Search</a>
    </div>

    <% if (results != null && !results.isEmpty()) { %>
        <ul class="note-list">
            <% for (Note note : results) {
                // Get content preview
                String content = note.getContent();
                String preview = content.length() > 100 ? content.substring(0, 100) + "..." : content;

                // Highlight query term if present
                if (query != null && !query.isEmpty()) {
                    String queryLower = query.toLowerCase();
                    String titleLower = note.getTitle().toLowerCase();
                    String previewLower = preview.toLowerCase();

                    int titleIndex = titleLower.indexOf(queryLower);
                    if (titleIndex >= 0) {
                        String titleStart = note.getTitle().substring(0, titleIndex);
                        String titleMatch = note.getTitle().substring(titleIndex, titleIndex + query.length());
                        String titleEnd = note.getTitle().substring(titleIndex + query.length());
                        // Make a highlighted version of the title
                        note.setTitle(titleStart + "<span class=\"highlight\">" + titleMatch + "</span>" + titleEnd);
                    }

                    int previewIndex = previewLower.indexOf(queryLower);
                    if (previewIndex >= 0) {
                        String previewStart = preview.substring(0, previewIndex);
                        String previewMatch = preview.substring(previewIndex, previewIndex + query.length());
                        String previewEnd = preview.substring(previewIndex + query.length());
                        // Make a highlighted version of the preview
                        preview = previewStart + "<span class=\"highlight\">" + previewMatch + "</span>" + previewEnd;
                    }
                }
            %>
                <li>
                    <a href="viewNote.html?id=<%= note.getId() %>">
                        <h3><%= note.getTitle() %></h3>
                        <p class="note-type"><%= note.getType() %></p>
                        <p class="note-preview"><%= preview %></p>
                    </a>
                </li>
            <% } %>
        </ul>
    <% } else { %>
        <p>No notes found matching your search.</p>
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
</div>

<jsp:include page="/footer.jsp"/>
</body>
</html>