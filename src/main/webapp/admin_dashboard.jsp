<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("user") == null) {
        response.sendRedirect("login.jsp?error=session_expired");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #4CAF50;
            color: white;
            padding: 15px;
            text-align: center;
        }

        h2 {
            margin-top: 0;
        }

        a {
            color: #4CAF50;
            text-decoration: none;
            font-weight: bold;
        }

        a:hover {
            text-decoration: underline;
        }

        .container {
            width: 80%;
            margin: 20px auto;
        }

        h3 {
            color: #333;
        }

        form {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        form label {
            font-weight: bold;
            margin-bottom: 5px;
        }

        form input[type="text"], form input[type="password"], form textarea, form select {
            width: 100%;
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
        }

        form button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        form button:hover {
            background-color: #45a049;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }

        table, th, td {
            border: 1px solid #ddd;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #f4f4f4;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .submission-file {
            color: #4CAF50;
            text-decoration: none;
        }

        .submission-file:hover {
            text-decoration: underline;
        }

        .empty-message {
            font-style: italic;
            color: #888;
        }

        .logout-link {
            display: inline-block;
            margin-top: 15px;
            font-weight: bold;
            text-decoration: none;
            color: #f44336;
        }

        .logout-link:hover {
            text-decoration: underline;
        }

        .message {
            font-weight: bold;
            padding: 10px;
            margin: 10px 0;
            border-radius: 5px;
        }

        .success-message {
            background-color: #d4edda;
            color: #155724;
        }

        .error-message {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>

<header>
    <h2>Admin Dashboard</h2>
    <a class="logout-link" href="logout">Logout</a>
</header>

<div class="container">
    <!-- Display Messages -->
    <c:if test="${not empty successMessage}">
        <p class="message success-message"><c:out value="${successMessage}" /></p>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <p class="message error-message"><c:out value="${errorMessage}" /></p>
    </c:if>

    <!-- Add User Form -->
    <h3>Add New User</h3>
    <form action="AdminServlet" method="post">
        <label>Username:</label>
        <input type="text" name="username" required><br><br>

        <label>Password:</label>
        <input type="password" name="password" required><br><br>

        <label>Role:</label>
        <select name="role" required>
            <option value="STUDENT">Student</option>
            <option value="TEACHER">Teacher</option>
        </select><br><br>

        <button type="submit">Add User</button>
    </form>

    <!-- Add Course Form -->
    <h3>Add New Course</h3>
    <form action="AdminServlet" method="post">
        <label>Course Name:</label>
        <input type="text" name="courseName" required><br><br>

        <label>Course Description:</label>
        <textarea name="courseDescription" required></textarea><br><br>

        <label>Instructor ID:</label>
        <input type="text" name="instructorId" required><br><br>

        <button type="submit">Add Course</button>
    </form>

    <!-- View All Assignments -->
    <h3>All Assignments</h3>
    <c:if test="${not empty assignments}">
        <table>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Course</th>
                <th>Deadline</th>
            </tr>
            <c:forEach var="assignment" items="${assignments}">
                <tr>
                    <td><c:out value="${assignment.id}"/></td>
                    <td><c:out value="${assignment.title}"/></td>
                    <td><c:out value="${assignment.description}"/></td>
                    <td><c:out value="${assignment.course.name}"/></td>
                    <td><c:out value="${assignment.deadline}"/></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${empty assignments}">
        <p>No assignments available.</p>
    </c:if>

    <!-- View All Submissions -->
    <h3>All Submissions</h3>
    <c:if test="${not empty submissions}">
        <table>
            <tr>
                <th>Assignment Title</th>
                <th>Student ID</th>
                <th>Submission Date</th>
                <th>File</th>
                <th>Status</th>
            </tr>
            <c:forEach var="submission" items="${submissions}">
                <tr>
                    <td><c:out value="${submission.assignment.title}"/></td>
                    <td><c:out value="${submission.student.id}"/></td>
                    <td><c:out value="${submission.submissionTime}"/></td>
                    <td><a class="submission-file" href="${submission.filePath}" target="_blank">View File</a></td>
                    <td>
                        <c:choose>
                            <c:when test="${submission.submissionTime != null}">
                                Submitted
                            </c:when>
                            <c:otherwise>
                                Not Submitted
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${empty submissions}">
        <p>No submissions available.</p>
    </c:if>
</div>

</body>
</html>
