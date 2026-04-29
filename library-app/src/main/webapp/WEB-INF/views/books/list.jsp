<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${pageTitle} – Library</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav>
    <span class="brand">📚 Library System</span>
    <a href="${pageContext.request.contextPath}/books"   class="active">Books</a>
    <a href="${pageContext.request.contextPath}/authors">Authors</a>
</nav>

<div class="container">

    <!-- Flash messages -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <!-- Stats -->
    <div class="stats-bar">
        <div class="stat-card">
            <div class="stat-num">${bookDetails.size()}</div>
            <div class="stat-lbl">Total Books</div>
        </div>
    </div>

    <div class="page-header">
        <h1>📖 All Books</h1>
        <a href="${pageContext.request.contextPath}/books/add" class="btn btn-primary">+ Add Book</a>
    </div>

    <div class="card">
        <c:choose>
            <c:when test="${empty bookDetails}">
                <p style="text-align:center; color:var(--muted); padding:2rem;">No books found.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Title</th>
                            <th>Author</th>
                            <th>Nationality</th>
                            <th>ISBN</th>
                            <th>Year</th>
                            <th>Genre</th>
                            <th>Pages</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%-- bookDetails is List<BookAuthorDTO> from the INNER JOIN query --%>
                        <c:forEach var="bd" items="${bookDetails}" varStatus="status">
                            <tr>
                                <td>${status.count}</td>
                                <td><strong>${bd.title}</strong></td>
                                <td>${bd.authorFullName}</td>
                                <td><span class="badge badge-blue">${bd.nationality}</span></td>
                                <td style="font-family:monospace; font-size:.85rem">${bd.isbn}</td>
                                <td>${bd.publicationYear}</td>
                                <td><span class="badge badge-green">${bd.genre}</span></td>
                                <td>${bd.pages}</td>
                                <td>
                                    <div class="actions">
                                        <a href="${pageContext.request.contextPath}/books/edit/${bd.bookId}"
                                           class="btn btn-warning btn-sm">✏ Edit</a>
                                        <a href="${pageContext.request.contextPath}/books/delete/${bd.bookId}"
                                           class="btn btn-danger btn-sm"
                                           onclick="return confirm('Delete this book?')">🗑 Del</a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>

</div>
</body>
</html>
