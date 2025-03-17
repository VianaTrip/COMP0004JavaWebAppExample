<%@ page import="uk.ac.ucl.model.Note" %>
<%@ page import="uk.ac.ucl.model.Category" %>
<%@ page import="uk.ac.ucl.model.TextNote" %>
<%@ page import="uk.ac.ucl.model.URLNote" %>
<%@ page import="uk.ac.ucl.model.ImageNote" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
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
        .applied-filters {
            display: flex;
            flex-wrap: wrap;
            gap: 5px;
            margin-top: 10px;
        }
        .filter-tag {
            display: inline-block;
            background-color: #e9ecef;
            color: #212529;
            padding: 3px 8px;
            border-radius: 15px;
            font-size: 0.85em;
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
            display: inline-block;
            background-color: #f8f9fa;
            padding: 2px 6px;
            border-radius: 3px;
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
        .no-results {
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 5px;
            text-align: center;
        }
        .note-item a {
            text-decoration: none;
            color: inherit;
        }
        .note-item a:hover {
            text-decoration: none;
        }
        .note-item {
            transition: transform 0.1s ease-in-out;
        }
        .note-item:hover {
            transform: translateX(5px);
            border-left: 3px solid #007bff;
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
            String[] selectedNoteTypes = (String[]) request.getAttribute("selectedNoteTypes");
            String selectedCategory = (String) request.getAttribute("selectedCategory");
            String selectedSort = (String) request.getAttribute("selectedSort");
            List<Category> categories = (List<Category>) request.getAttribute("categories");

            Category selectedCategoryObj = null;
            if (selectedCategory != null && !selectedCategory.isEmpty()) {
                for (Category cat : categories) {
                    if (cat.getId().equals(selectedCategory)) {
                        selectedCategoryObj = cat;
                        break;
                    }
                }
            }
        %>
        <p>Search results for: <strong><%= query %></strong></p>
        <p>Found <%= results.size() %> matching notes.</p>

        <div class="applied-filters">
            <% if (selectedNoteTypes != null && selectedNoteTypes.length > 0) { %>
                <span class="filter-tag">Types: <%= String.join(", ", selectedNoteTypes) %></span>
            <% } %>

            <% if (selectedCategoryObj != null) { %>
                <span class="filter-tag">Category: <%= selectedCategoryObj.getName() %></span>
            <% } %>

            <% if (selectedSort != null && !selectedSort.isEmpty()) {
                String sortLabel = "";
                switch(selectedSort) {
                    case "title": sortLabel = "Title"; break;
                    case "newest": sortLabel = "Newest First"; break;
                    case "oldest": sortLabel = "Oldest First"; break;
                    case "relevance":
                    default: sortLabel = "Relevance"; break;
                }
            %>
                <span class="filter-tag">Sorted by: <%= sortLabel %></span>
            <% } %>
        </div>

        <div style="margin-top: 15px;">
            <a href="searchNotes.html" class="button">New Search</a>
            <a href="notes.html" class="button">Back to Notes</a>
        </div>
    </div>

    <% if (results != null && !results.isEmpty()) { %>
        <ul class="note-list">
            <% for (Note note : results) {
                // Get content preview based on note type
                String preview = "";

                if (note instanceof TextNote) {
                    TextNote textNote = (TextNote) note;
                    String text = textNote.getText();
                    preview = text.length() > 150 ? text.substring(0, 150) + "..." : text;
                } else if (note instanceof URLNote) {
                    URLNote urlNote = (URLNote) note;
                    preview = urlNote.getUrl();
                    if (!urlNote.getDescription().isEmpty()) {
                        preview += "\n" + urlNote.getDescription().substring(0,
                            Math.min(urlNote.getDescription().length(), 100));
                        if (urlNote.getDescription().length() > 100) {
                            preview += "...";
                        }
                    }
                } else if (note instanceof ImageNote) {
                    ImageNote imageNote = (ImageNote) note;
                    preview = "Image: " + (imageNote.getCaption().isEmpty() ?
                        "[No caption]" : imageNote.getCaption());
                }

                // Highlight query term if present
                String displayTitle = note.getTitle();
                if (query != null && !query.isEmpty()) {
                    String queryLower = query.toLowerCase();
                    String titleLower = displayTitle.toLowerCase();
                    String previewLower = preview.toLowerCase();

                    int titleIndex = titleLower.indexOf(queryLower);
                    if (titleIndex >= 0) {
                        String titleStart = displayTitle.substring(0, titleIndex);
                        String titleMatch = displayTitle.substring(titleIndex, titleIndex + query.length());
                        String titleEnd = displayTitle.substring(titleIndex + query.length());
                        // Create highlighted version for display
                        displayTitle = titleStart + "<span class=\"highlight\">" + titleMatch + "</span>" + titleEnd;
                    }

                    int previewIndex = previewLower.indexOf(queryLower);
                    if (previewIndex >= 0) {
                        String previewStart = preview.substring(0, previewIndex);
                        String previewMatch = preview.substring(previewIndex, previewIndex + query.length());
                        String previewEnd = preview.substring(previewIndex + query.length());
                        // Create highlighted version for display
                        preview = previewStart + "<span class=\"highlight\">" + previewMatch + "</span>" + previewEnd;
                    }
                }