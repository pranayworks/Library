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
    <a href="${pageContext.request.contextPath}/books">Books</a>
    <a href="${pageContext.request.contextPath}/authors" class="active">Authors</a>
</nav>

<div class="container">

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="page-header">
        <h1>${pageTitle}</h1>
        <a href="${pageContext.request.contextPath}/authors" class="btn btn-primary">← Back to List</a>
    </div>

    <div class="card">
        <c:choose>
            <c:when test="${author.id != null}">
                <c:set var="actionUrl"
                       value="${pageContext.request.contextPath}/authors/update/${author.id}"/>
            </c:when>
            <c:otherwise>
                <c:set var="actionUrl"
                       value="${pageContext.request.contextPath}/authors/save"/>
            </c:otherwise>
        </c:choose>

        <form:form action="${actionUrl}" method="post" modelAttribute="author">

            <div class="form-grid">

                <div class="form-group">
                    <label for="firstName">First Name *</label>
                    <form:input path="firstName" id="firstName" cssClass="form-control"
                                placeholder="e.g. George"/>
                    <form:errors path="firstName" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <label for="lastName">Last Name *</label>
                    <form:input path="lastName" id="lastName" cssClass="form-control"
                                placeholder="e.g. Orwell"/>
                    <form:errors path="lastName" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <label for="nationality">Nationality *</label>
                    <form:input path="nationality" id="nationality" cssClass="form-control"
                                placeholder="e.g. British"/>
                    <form:errors path="nationality" cssClass="form-error"/>
                </div>

                <div class="form-group">
                    <label for="birthYear">Birth Year *</label>
                    <form:input path="birthYear" id="birthYear" cssClass="form-control"
                                type="number" placeholder="e.g. 1903"/>
                    <form:errors path="birthYear" cssClass="form-error"/>
                </div>

            </div>

            <div style="margin-top:1.5rem; display:flex; gap:.8rem;">
                <button type="submit" class="btn btn-success">
                    ${author.id != null ? '💾 Update Author' : '➕ Add Author'}
                </button>
                <a href="${pageContext.request.contextPath}/authors" class="btn btn-primary">Cancel</a>
            </div>

        </form:form>
    </div>

</div>
</body>
</html>
