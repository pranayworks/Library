<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
    <a href="${pageContext.request.contextPath}/books">Books</a>
    <a href="${pageContext.request.contextPath}/authors" class="active">Authors</a>
</nav>

<div class="container">

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="stats-bar">
        <div class="stat-card">
            <div class="stat-num">${authors.size()}</div>
            <div class="stat-lbl">Authors</div>
        </div>
    </div>

    <div class="page-header">
        <h1>✍️ All Authors</h1>
        <a href="${pageContext.request.contextPath}/authors/add" class="btn btn-primary">+ Add Author</a>
    </div>

    <div class="card">
        <c:choose>
            <c:when test="${empty authors}">
                <p style="text-align:center;color:var(--muted);padding:2rem;">No authors found.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Name</th>
                            <th>Nationality</th>
                            <th>Birth Year</th>
                            <th>Books Count</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="author" items="${authors}" varStatus="status">
                            <tr>
                                <td>${status.count}</td>
                                <td><strong>${author.fullName}</strong></td>
                                <td><span class="badge badge-blue">${author.nationality}</span></td>
                                <td>${author.birthYear}</td>
                                <td>
                                    <span class="badge badge-purple">${author.books.size()} book(s)</span>
                                </td>
                                <td>
                                    <div class="actions">
                                        <a href="${pageContext.request.contextPath}/authors/edit/${author.id}"
                                           class="btn btn-warning btn-sm">✏ Edit</a>
                                        <a href="${pageContext.request.contextPath}/authors/delete/${author.id}"
                                           class="btn btn-danger btn-sm"
                                           onclick="return confirm('Delete author? This will also delete their books.')">
                                           🗑 Del</a>
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
