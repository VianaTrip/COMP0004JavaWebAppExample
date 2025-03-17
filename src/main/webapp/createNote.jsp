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
        .tab-container {
            margin-bottom: 20px;
        }
        .tab {
            display: inline-block;
            padding: 10px 15px;
            background-color: #f8f9fa;
            cursor: pointer;
            border: 1px solid #ddd;
            border-bottom: none;
            border-radius: 4px 4px 0 0;
        }
        .tab.active {
            background-color: #fff;
            border-bottom: 1px solid #fff;
            margin-bottom: -1px;
            position: relative;
            z-index: 1;
        }
        .tab-content {
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 0 0 4px 4px;
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
    <h1>Create New Note</h1>

    <%
        String noteType = (String) request.getAttribute("noteType");
        List<Category> categories = (List<Category>) request.getAttribute("categories");

        if (noteType == null) {
            noteType = "TEXT";
        }
    %>

    <div class="form-container">
        <div class="tab-container">
            <a href="createNote.html?type=TEXT" class="tab <%= "TEXT".equals(noteType) ? "active" : "" %>">Text Note</a>
            <a href="createNote.html?type=URL" class="tab <%= "URL".equals(noteType) ? "active" : "" %>">URL Note</a>
            <a href="createNote.html?type=IMAGE" class="tab <%= "IMAGE".equals(noteType) ? "active" : "" %>">Image Note</a>
        </div>

        <div class="tab-content">
            <form method="post" action="<%= "IMAGE".equals(noteType) ? "uploadImage.html" : "createNote.html" %>" enctype="<%= "IMAGE".equals(noteType) ? "multipart/form-data" : "application/x-www-form-urlencoded" %>">
                <input type="hidden" name="type" value="<%= noteType %>">

                <div class="form-group">
                    <label for="title">Title:</label>
                    <input type="text" id="title" name="title" required>
                </div>

                <% if ("TEXT".equals(noteType)) { %>
                    <div class="form-group">
                        <label for="text">Content:</label>
                        <textarea id="text" name="text"></textarea>
                    </div>
                <% } else if ("URL".equals(noteType)) { %>
                    <div class="form-group">
                        <label for="url">URL:</label>
                        <input type="text" id="url" name="url" required>
                    </div>

                    <div class="form-group">
                        <label for="description">Description:</label>
                        <textarea id="description" name="description"></textarea>
                    </div>
                <% } else if ("IMAGE".equals(noteType)) { %>
                    <div class="form-group">
                        <label for="imageFile">Image File:</label>
                        <input type="file" id="imageFile" name="imageFile" accept="image/*" required>
                    </div>

                    <div class="form-group">
                        <label for="imageCaption">Caption:</label>
                        <textarea id="imageCaption" name="imageCaption"></textarea>
                    </div>
                <% } %>

                <div class="form-group">
                    <label>Categories:</label>
                    <div class="category-options">
                        <% if (categories != null && !categories.isEmpty()) {
                            for (Category category : categories) {
                        %>
                            <div class="category-option">
                                <input type="checkbox" id="category-<%= category.getId() %>"
                                       name="categories" value="<%= category.getId() %>">
                                <label for="category-<%= category.getId() %>"><%= category.getName() %></label>
                            </div>
                        <% } } else { %>
                            <p>No categories available. <a href="manageCategories.html">Create some categories</a>.</p>
                        <% } %>
                    </div>
                </div>

                <div class="form-group">
                    <button type="submit" class="button">Create Note</button>
                    <a href="notes.html" class="button button-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>