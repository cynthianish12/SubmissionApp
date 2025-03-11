<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Available Assignments</title>
</head>
<body>
<h2>Available Assignments</h2>

    <c:if test="${not empty assignments}">
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Course</th>
                <th>Class</th>
                <th>Deadline</th>
            </tr>
            <c:forEach var="assignment" items="${assignments}">
                <tr>
                    <td><c:out value="${assignment.id}"/></td>
                    <td><c:out value="${assignment.title}"/></td>
                    <td><c:out value="${assignment.description}"/></td>
                    <td><c:out value="${assignment.course.name}"/></td>
                    <td><c:out value="${assignment.classSelection}"/></td>
                    <td><c:out value="${assignment.deadline}"/></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${empty assignments}">
        <p>No assignments created yet.</p>
    </c:if>
    <a href="submit-assignment.jsp">Submit Assignment</a> | <a href="submissions.jsp">View Submissions</a>
</body>
</html>