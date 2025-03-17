<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="/meta.jsp"/>
    <title>Search Notes</title>
    <style>
        .search-form {
            margin: 20px 0;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 5px;
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
            font-size: 16px;
        }
        .button-secondary {
            background-color: #6c757d;
        }
    </style>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
    <h1>Search Notes</h1>

    <div class="search-form">
        <form method="post" action="searchNotes.html">
            <div class="form-group">
                <label for="query">Search Query:</label>
                <input type="text" id="query" name="query" placeholder="Enter search terms..." required>
            </div>

            <div class="form-group">
                <button type="submit" class="button">Search</button>
                <a href="notes.html" class="button button-secondary">Back to Notes</a>
            </div>
        </form>
    </div>

    <p>Search will find matches in note titles and content.</p>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>