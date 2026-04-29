<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"    uri="jakarta.tags.core"   %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="page-header">
        <h1>${pageTitle}</h1>
        <a href="${pageContext.request.contextPath}/books" class="btn btn-primary">← Back to List</a>
    </div>

    <div class="card">
        <%-- Determine action URL: if book.id exists → update, else → save --%>
        <c:choose>
            <c:when test="${book.id != null}">
                <c:set var="actionUrl"
                       value="${pageContext.request.contextPath}/books/update/${book.id}"/>
            </c:when>
            <c:otherwise>
                <c:set var="actionUrl"
                       value="${pageContext.request.contextPath}/books/save"/>
            </c:otherwise>
        </c:choose>

        <form:form action="${actionUrl}" method="post" modelAttribute="book">

            <div class="form-grid">

                <div class="form-group">
                    <label for="title">Title *</label>
                    <form:input path="title" id="title" cssClass="form-control"
                                placeholder="e.g. The Great Gatsby"/>
                    <form:errors path="title" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <label for="isbn">ISBN *</label>
                    <form:input path="isbn" id="isbn" cssClass="form-control"
                                placeholder="e.g. 978-0743273565"/>
                    <form:errors path="isbn" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <label for="publicationYear">Publication Year *</label>
                    <form:input path="publicationYear" id="publicationYear"
                                cssClass="form-control" type="number"
                                placeholder="e.g. 1925"/>
                    <form:errors path="publicationYear" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <label for="genre">Genre *</label>
                    <form:input path="genre" id="genre" cssClass="form-control"
                                placeholder="e.g. Literary Fiction"/>
                    <form:errors path="genre" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <label for="pages">Pages *</label>
                    <form:input path="pages" id="pages" cssClass="form-control"
                                type="number" placeholder="e.g. 300"/>
                    <form:errors path="pages" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <label for="authorId">Author *</label>
                    <select name="authorId" id="authorId" class="form-control" required>
                        <option value="">-- Select Author --</option>
                        <c:forEach var="author" items="${authors}">
                            <option value="${author.id}"
                                <c:if test="${author.id == selectedAuthorId}">selected</c:if>>
                                ${author.fullName} (${author.birthYear})
                            </option>
                        </c:forEach>
                    </select>
                </div>

            </div>

            <div style="margin-top:1.5rem; display:flex; gap:.8rem;">
                <button type="submit" class="btn btn-success">
                    ${book.id != null ? '💾 Update Book' : '➕ Add Book'}
                </button>
                <a href="${pageContext.request.contextPath}/books" class="btn btn-primary">Cancel</a>
            </div>

        </form:form>
    </div>

</div>
</body>
</html>
