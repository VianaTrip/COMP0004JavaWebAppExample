<div class="search-form">
    <form method="post" action="searchNotes.html">
        <div class="form-group">
            <label for="query">Search Query:</label>
            <input type="text" id="query" name="query" placeholder="Enter search terms..." required>
        </div>

        <div class="search-options">
            <div class="search-option">
                <label>Note Types to Search:</label>
                <div class="checkbox-group">
                    <div class="checkbox-item">
                        <input type="checkbox" id="searchText" name="noteTypes" value="TEXT" checked>
                        <label for="searchText">Text Notes</label>
                    </div>
                    <div class="checkbox-item">
                        <input type="checkbox" id="searchURL" name="noteTypes" value="URL" checked>
                        <label for="searchURL">URL Notes</label>
                    </div>
                    <div class="checkbox-item">
                        <input type="checkbox" id="searchImage" name="noteTypes" value="IMAGE" checked>
                        <label for="searchImage">Image Notes</label>
                    </div>
                </div>
            </div>

            <div class="search-option">
                <label for="categoryFilter">Filter by Category:</label>
                <select id="categoryFilter" name="categoryFilter">
                    <option value="">All Categories</option>
                    <%
                        List<Category> categories = (List<Category>) request.getAttribute("categories");
                        if (categories != null) {
                            for (Category category : categories) {
                    %>
                        <option value="<%= category.getId() %>"><%= category.getName() %></option>
                    <% }} %>
                </select>
            </div>

            <div class="search-option">
                <label for="sortBy">Sort Results By:</label>
                <select id="sortBy" name="sortBy">
                    <option value="relevance">Relevance</option>
                    <option value="title">Title</option>
                    <option value="newest">Newest First</option>
                    <option value="oldest">Oldest First</option>
                </select>
            </div>
        </div>

        <div class="form-group" style="margin-top: 20px;">
            <button type="submit" class="button">Search</button>
            <a href="notes.html" class="button button-secondary">Back to Notes</a>
        </div>
    </form>
</div>

<p>Search will find matches in note titles and content. You can filter by note type and category.</p>